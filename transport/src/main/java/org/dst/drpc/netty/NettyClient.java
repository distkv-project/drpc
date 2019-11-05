package org.dst.drpc.netty;

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
import org.dst.drpc.api.AbstractClient;
import org.dst.drpc.api.async.AsyncResponse;
import org.dst.drpc.api.async.DefaultAsyncResponse;
import org.dst.drpc.api.async.DefaultResponse;
import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.codec.DstCodec;
import org.dst.drpc.codec.ProtoBufSerialization;
import org.dst.drpc.common.URL;
import org.dst.drpc.exception.DstException;
import org.dst.drpc.exception.TransportException;
import org.dst.drpc.netty.codec.NettyDecoder;
import org.dst.drpc.netty.codec.NettyEncoder;

/**
 *
 */
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
                  throw new DstException(
                      "NettyChannelHandler: unsupported message type when encode: " + object
                          .getClass());
                }
                processResponse(ctx, (Response) object);
              }

              private void processResponse(ChannelHandlerContext ctx, Response response) {
                // 这一步转换一定不会出错
                AsyncResponse future = (AsyncResponse) getResponseFuture(response.getRequestId());
                // 使用setValue通知异步response成功
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
      // todo : log or retry
      throw new TransportException("NettyClient: connect().sync() interrupted", i);
    }
    // 标志当前的Channel已经打开
    // 保存当前的netty channel。
    clientChannel = future.channel();
    // 新起一个线程去监听close事件
    executor.submit(() -> {
      try {
        clientChannel.closeFuture().sync();
      } catch (Exception e) {
        // todo : log
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
        throw new DstException("ClientChannel closed");
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
