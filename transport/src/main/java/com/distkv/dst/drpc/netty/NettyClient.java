package com.distkv.dst.drpc.netty;

import com.distkv.dst.drpc.api.AbstractClient;
import com.distkv.dst.drpc.codec.DstCodec;
import com.distkv.dst.drpc.codec.ProtoBufSerialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.distkv.dst.drpc.api.async.AsyncResponse;
import com.distkv.dst.drpc.api.async.DefaultAsyncResponse;
import com.distkv.dst.drpc.api.async.DefaultResponse;
import com.distkv.dst.drpc.api.async.Request;
import com.distkv.dst.drpc.api.async.Response;
import com.distkv.dst.drpc.common.URL;
import com.distkv.dst.drpc.exception.DrpcException;
import com.distkv.dst.drpc.exception.TransportException;
import com.distkv.dst.drpc.netty.codec.NettyDecoder;
import com.distkv.dst.drpc.netty.codec.NettyEncoder;

public class NettyClient extends AbstractClient {

  private io.netty.channel.Channel clientChannel;
  private NioEventLoopGroup nioEventLoopGroup;
  private ExecutorService executor;

  public NettyClient(URL serverUrl) {
    super(serverUrl, new DstCodec(new ProtoBufSerialization()));
    executor = Executors.newSingleThreadExecutor();
    nioEventLoopGroup = new NioEventLoopGroup();
  }

  @Override
  protected void doOpen() {
    Bootstrap bootstrap = new Bootstrap();
    int connectTimeoutMillis = getUrl().getInt("CONNECT_TIMEOUT_MILLIS", 3000);
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis);
    bootstrap.option(ChannelOption.TCP_NODELAY, true);
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.group(nioEventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new NettyDecoder());
            pipeline.addLast("encoder", new NettyEncoder());
            pipeline.addLast("handler", new ChannelDuplexHandler() {

              @Override
              public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Object object = getCodec().decode((byte[]) msg);
                if (!(object instanceof Response)) {
                  throw new DrpcException(
                      "NettyChannelHandler: unsupported message type when encode: " + object
                          .getClass());
                }
                processResponse((Response) object);
              }

              private void processResponse(Response response) {
                Response future = getResponseFuture(response.getRequestId());
                if (response.getThrowable() != null) {
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
      future = bootstrap.connect(getUrl().getHost(), getUrl().getPort()).sync();
    } catch (InterruptedException i) {
      close();
      throw new TransportException("NettyClient: connect().sync() interrupted", i);
    }

    clientChannel = future.channel();
    executor.submit(() -> {
      try {
        clientChannel.closeFuture().sync();
      } catch (Exception e) {
        logger.error("Client occurs error when close.", e);
      } finally {
        close();
      }
    });
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
    executor.shutdown();
  }

  @Override
  public Response invoke(Request request) {
    AsyncResponse response = new DefaultAsyncResponse(request.getRequestId());
    addCurrentTask(request.getRequestId(), response);
    try {
      byte[] msg = getCodec().encode(request);
      if (clientChannel.isActive()) {
        clientChannel.writeAndFlush(msg);
      } else {
        throw new DrpcException("ClientChannel closed");
      }
      return response;
    } catch (Exception e) {
      Response errorResponse = new DefaultResponse();
      errorResponse.setRequestId(request.getRequestId());
      errorResponse
          .setThrowable(new TransportException("NettyClient: response.getValue interrupted!"));
      return errorResponse;
    }
  }
}
