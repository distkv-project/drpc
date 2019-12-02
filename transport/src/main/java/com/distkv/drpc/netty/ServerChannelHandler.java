package com.distkv.drpc.netty;

import com.distkv.drpc.api.Handler;
import com.distkv.drpc.api.async.Request;
import com.distkv.drpc.api.async.Response;
import com.distkv.drpc.common.Void;
import com.distkv.drpc.exception.DrpcException;
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
    Object object = nettyServer.getCodec().decode((byte[]) msg);
    if (!(object instanceof Request)) {
      throw new DrpcException(
            "ServerChannelHandler: unsupported message type when decode: " + object.getClass());
    }
    if (nettyServer.getExecutor() != null) {
      nettyServer.getExecutor().execute(() -> processRequest(ctx, (Request) object));
    } else {
      processRequest(ctx, (Request) object);
    }
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
    byte[] msg = nettyServer.getCodec().encode(response);
    if (ctx.channel().isActive()) {
      return ctx.channel().writeAndFlush(msg);
    }
    return null;
  }

}
