package org.dst.drpc.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.CompletableFuture;
import org.dst.drpc.api.AbstractChannel;
import org.dst.drpc.api.Channel;
import org.dst.drpc.api.Handler;
import org.dst.drpc.api.async.DefaultResponse;
import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.api.support.RpcContext;
import org.dst.drpc.common.Void;
import org.dst.drpc.exception.DstException;

/**
 * @author zrj CreateDate: 2019/11/2
 */
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
      throw new DstException(
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
    if(value == null || value instanceof Void) {
      return;
    }
    if(value instanceof CompletableFuture) {
      CompletableFuture future = (CompletableFuture) value;
      future.whenComplete((r, t) -> {
        if(t != null) {
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

  private RpcContext createRpcContext(ChannelHandlerContext ctx, Request request) {
    Channel channel = new AbstractChannel() {

      @Override
      public void send(Object message) {
        Response response = new DefaultResponse();
        response.setRequestId(request.getRequestId());
        if(message instanceof Exception) {
          response.setThrowable((Throwable) message);
        } else {
          response.setValue(message);
        }
        sendResponse(ctx, response);
      }
    };
    return RpcContext.createRpcContext(channel);
  }

  private ChannelFuture sendResponse(ChannelHandlerContext ctx, Response response) {
    byte[] msg = nettyServer.getCodec().encode(response);
    if (ctx.channel().isActive()) {
      return ctx.channel().writeAndFlush(msg);
    }
    return null;
  }

}
