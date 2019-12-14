package com.distkv.drpc.benchmark.common.consumer;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.INTERNAL_SERVER_ERROR;

import com.distkv.drpc.benchmark.common.BenchmarkException;
import com.distkv.drpc.benchmark.common.IService;
import com.distkv.drpc.benchmark.common.generated.CommonProtocol.Status;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.GetRequest;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.PutRequest;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.PutResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class HttpProcessHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpProcessHandler.class);
  private IService service;

  public HttpProcessHandler(IService service) {
    this.service = service;
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
    long start = System.currentTimeMillis();
    String keyValue = UUID.randomUUID().toString();
    String key = keyValue.substring(0, 14);
    String value = key.substring(14);

    // put
    PutRequest putRequest = PutRequest.newBuilder().setKey(key)
        .setValue(value)
        .build();
    try {
      PutResponse response = service.put(putRequest).get();
      if (response.getStatus() != Status.OK) {
        throw new BenchmarkException("Response status is not 'OK': " + response.getStatus());
      }
    } catch (Exception e) {
      FullHttpResponse error =
          new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
      error.headers().add(HttpHeaderNames.CONTENT_LENGTH, 0);
      ctx.writeAndFlush(error);
      LOGGER.info("Request result:failure cost:{} ms", System.currentTimeMillis() - start, e);
    }

    // get
    GetRequest getRequest = GetRequest.newBuilder()
        .setKey(key)
        .build();
    service.get(getRequest)
        .whenComplete((actual, t) -> {
          if (t == null && actual.getStatus() == Status.OK && actual.getValue().equals(value)) {
            FullHttpResponse ok =
                new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled
                    .copiedBuffer("OK\n", CharsetUtil.UTF_8));
            ok.headers().add(HttpHeaderNames.CONTENT_LENGTH, 3);
            ctx.writeAndFlush(ok);
            if (LOGGER.isInfoEnabled()) {
              LOGGER.info("Request result:success cost:{} ms", System.currentTimeMillis() - start);
            }
          } else {
            FullHttpResponse error =
                new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
            error.headers().add(HttpHeaderNames.CONTENT_LENGTH, 0);
            ctx.writeAndFlush(error);
            LOGGER.info("Request result:failure cost:{} ms", System.currentTimeMillis() - start, t);
          }
        });
  }

}
