package org.dousi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.dousi.api.AbstractServer;
import org.dousi.api.Handler;
import org.dousi.codec.DousiCodec;
import org.dousi.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class NettyServer extends AbstractServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

  private io.netty.channel.Channel serverChannel;
  private NioEventLoopGroup bossGroup;
  private NioEventLoopGroup workerGroup;


  public NettyServer(ServerConfig serverConfig, List<Handler> handlers) {
    super(serverConfig, new DousiCodec());
    handlers.forEach((handler) -> getRoutableHandler().registerHandler(handler));
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();
  }

  @Override
  protected void doOpen() {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) {
            LOGGER.info("new channel coming in...");
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new ProtobufVarint32FrameDecoder());
            pipeline.addLast("encoder", new ProtobufVarint32LengthFieldPrepender());
            pipeline.addLast("handler", new ServerChannelHandler(NettyServer.this));
          }
        });
    serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
    serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

    ChannelFuture f = serverBootstrap.bind(getConfig().getPort()).syncUninterruptibly();
    serverChannel = f.channel();
  }

  @Override
  protected void doClose() {
    if (serverChannel != null) {
      serverChannel.close();
    }
    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
      bossGroup = null;
    }
    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
      workerGroup = null;
    }
  }

}
