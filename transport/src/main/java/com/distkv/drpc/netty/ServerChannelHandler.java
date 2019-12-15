package com.distkv.drpc.netty;

import com.distkv.drpc.api.Handler;
import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;
import com.distkv.drpc.codec.Codec.DataTypeEnum;
import com.distkv.drpc.common.Void;
import com.distkv.drpc.exception.DrpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerChannelHandler extends ChannelDuplexHandler {

  private Handler handler;
  private NettyServer nettyServer;
  private ExecutorService executorService = Executors.newSingleThreadExecutor();


  public ServerChannelHandler(NettyServer nettyServer) {
    this.nettyServer = nettyServer;
    this.handler = nettyServer.getRoutableHandler();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf byteBuf = (ByteBuf) msg;
    byte[] data = new byte[byteBuf.readableBytes()];
    byteBuf.readBytes(data);
    Object object = nettyServer.getCodec().decode(data, DataTypeEnum.REQUEST);
    if (!(object instanceof Request)) {
      throw new DrpcException(
          "ServerChannelHandler: unsupported message type when decode: " + object.getClass());
    }
    executorService.submit(() -> processRequest(ctx, (Request) object));
  }

  private void processRequest(ChannelHandlerContext ctx, Request request) {
    Response response = (Response) handler.handle(request);
    response.setRequestId(request.getRequestId());
    Object value = response.getValue();
    if (value == null || value instanceof Void) {
      return;
    }
    if (value instanceof CompletableFuture) {
      CompletableFuture future = (CompletableFuture) value;
      future.whenComplete((r, t) -> {
        if (t != null) {
          response.setThrowable((Throwable) t);
        } else {
          response.setValue(r);
        }
        sendResponse(ctx, response);
      });
    } else {
      sendResponse(ctx, response);
    }
  }

  private ChannelFuture sendResponse(ChannelHandlerContext ctx, Response response) {
    try {
      byte[] msg = nettyServer.getCodec().encode(response);
      ByteBuf byteBuf = ctx.channel().alloc().heapBuffer();
      byteBuf.writeBytes(msg);
      if (ctx.channel().isActive()) {
        return ctx.channel().writeAndFlush(byteBuf).sync();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

}
