package org.drpc.netty;

import org.drpc.api.AbstractClient;
import org.drpc.api.DefaultAsyncResponse;
import org.drpc.api.Request;
import org.drpc.codec.Codec;
import org.drpc.codec.DrpcCodec;
import org.drpc.api.AsyncResponse;
import org.drpc.api.Response;
import org.drpc.config.ClientConfig;
import org.drpc.constants.GlobalConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.drpc.exception.DrpcConnectionRefusedException;
import org.drpc.exception.DrpcException;
import org.drpc.exception.DrpcRuntimeException;
import org.drpc.exception.DrpcTransportException;
import org.drpc.exception.DrpcCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrpcClient extends AbstractClient {

  private static Logger logger = LoggerFactory.getLogger(DrpcClient.class);

  private io.netty.channel.Channel clientChannel;
  private NioEventLoopGroup nioEventLoopGroup;

  public DrpcClient(ClientConfig clientConfig) {
    super(clientConfig, new DrpcCodec());
    nioEventLoopGroup = new NioEventLoopGroup(GlobalConstants.THREAD_NUMBER + 1);
  }

  @Override
  protected void doOpen() {
    Bootstrap bootstrap = new Bootstrap();
    int connectTimeoutMillis = getConfig().getTimeout() > 0 ? getConfig().getTimeout()
        : GlobalConstants.DEFAULT_CLIENT_TIMEOUT;
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis);
    bootstrap.option(ChannelOption.TCP_NODELAY, true);
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    bootstrap.group(nioEventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new ProtobufVarint32FrameDecoder());
            pipeline.addLast("encoder", new ProtobufVarint32LengthFieldPrepender());
            pipeline.addLast("handler", new ChannelDuplexHandler() {

              @Override
              public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf byteBuf = (ByteBuf) msg;
                byte[] data = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(data);
                byteBuf.release();
                Object object = getCodec().decode(data, Codec.DataTypeEnum.RESPONSE);
                if (!(object instanceof Response)) {
                  throw new DrpcException(
                      "NettyChannelHandler: unsupported message type when encode: " + object
                          .getClass());
                }
                processResponse((Response) object);
              }

              private void processResponse(Response response) {
                Response future = getResponseFuture(response.getRequestId());
                future.setStatus(response.getStatus());
                if (response.isError()) {
                  future.setThrowable(response.getThrowable());
                } else {
                  future.setValue(response.getValue());
                }
              }
            });
          }
        });
    ChannelFuture future;
    try {
      future = bootstrap.connect(getConfig().getServerIp(), getConfig().getServerPort()).sync();
    } catch (InterruptedException i) {
      close();
      throw new DrpcTransportException("NettyClient: connect().sync() interrupted", i);
    } catch (Exception connectedFailed) {
      throw new DrpcConnectionRefusedException("Connected failed. ", connectedFailed);
    }

    clientChannel = future.channel();
  }

  @Override
  protected void doClose() {
    if (!isOpen()) {
      return;
    }
    if (clientChannel != null) {
      clientChannel.close();
    }
    if (nioEventLoopGroup != null) {
      nioEventLoopGroup.shutdownGracefully();
    }
  }

  @Override
  public Response invoke(Request request) {
    AsyncResponse response = new DefaultAsyncResponse(request.getRequestId());
    addCurrentTask(request.getRequestId(), response);
    try {
      byte[] msg = getCodec().encode(request);
      ByteBuf byteBuf = clientChannel.alloc().heapBuffer();
      byteBuf.writeBytes(msg);
      if (clientChannel.isActive()) {
        clientChannel.writeAndFlush(byteBuf).sync();
      } else {
        throw new DrpcException("ClientChannel closed");
      }
      return response;
    } catch (InterruptedException e) {
      logger.error("NettyClient: response.getValue interrupted!");
      throw new DrpcRuntimeException("NettyClient: response.getValue interrupted!");
    } catch (DrpcCodecException | IllegalArgumentException e) {
      response.setThrowable(e);
      return response;
    }
  }
}
