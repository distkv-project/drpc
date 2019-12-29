package com.distkv.drpc.netty;

import com.distkv.drpc.api.AbstractClient;
import com.distkv.drpc.api.AsyncResponse;
import com.distkv.drpc.api.DefaultAsyncResponse;
import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;
import com.distkv.drpc.codec.Codec.DataTypeEnum;
import com.distkv.drpc.codec.DrpcCodec;
import com.distkv.drpc.codec.ProtoBufSerialization;
import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.constants.GlobalConstants;
import com.distkv.drpc.exception.CodecException;
import com.distkv.drpc.exception.DrpcException;
import com.distkv.drpc.exception.TransportException;
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

public class NettyClient extends AbstractClient {

  private io.netty.channel.Channel clientChannel;
  private NioEventLoopGroup nioEventLoopGroup;

  public NettyClient(ClientConfig clientConfig) {
    super(clientConfig, new DrpcCodec(new ProtoBufSerialization()));
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
                Object object = getCodec().decode(data, DataTypeEnum.RESPONSE);
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
      throw new TransportException("NettyClient: connect().sync() interrupted", i);
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
    } catch (CodecException e) {
      response.setThrowable(new CodecException("NettyClient: failed encode.", e));
      return response;
    } catch (InterruptedException e) {
      response.setThrowable(new TransportException("NettyClient: response.getValue interrupted!", e));
      return response;
    } catch (IllegalArgumentException e) {
      response.setThrowable(new TransportException(e));
      return response;
    }
  }
}
