package org.drpc.netty;

import org.drpc.api.Request;
import org.drpc.codec.Codec;
import org.drpc.common.Void;
import org.drpc.exception.DrpcException;
import org.drpc.api.Handler;
import org.drpc.api.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

public class ServerChannelHandler extends ChannelDuplexHandler {

  private Handler handler;
  private NettyServer nettyServer;

  public ServerChannelHandler(NettyServer nettyServer) {
    this.nettyServer = nettyServer;
    this.handler = nettyServer.getRoutableHandler();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf byteBuf = (ByteBuf) msg;
    byte[] data = new byte[byteBuf.readableBytes()];
    byteBuf.readBytes(data);
    byteBuf.release();
    Object object = nettyServer.getCodec().decode(data, Codec.DataTypeEnum.REQUEST);
    if (!(object instanceof Request)) {
      throw new DrpcException(
          "ServerChannelHandler: unsupported message type when decode: " + object.getClass());
    }
    String sessionStr = new String(((Request)object).getSessionID());
    if (nettyServer.getExecutor() != null) {
      if (sessionStr.hashCode() != 0) {
        nettyServer.getExecutor().submit(sessionStr.hashCode(), () -> processRequest(ctx, (Request) object));
      } else {
        nettyServer.getExecutor().submit(() -> processRequest(ctx, (Request) object));
      }
    } else {
      processRequest(ctx, (Request) object);
    }
  }

  private void processRequest(ChannelHandlerContext ctx, Request request) {
    Response response = (Response) handler.handle(request);
    response.setRequestId(request.getRequestId());
    if (response.getThrowable() != null) {
      sendResponse(ctx, response);
      return;
    }
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
