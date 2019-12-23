package com.distkv.drpc.test.benchmark;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.INTERNAL_SERVER_ERROR;

import com.distkv.drpc.test.common.BenchmarkIService;
import com.distkv.drpc.test.common.DymmyData;
import com.distkv.drpc.test.common.MD5Utils;
import com.distkv.drpc.test.generated.BenchmarkProtocol;
import com.google.protobuf.ByteString;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class HttpProcessHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpProcessHandler.class);
  private BenchmarkIService service;

  public HttpProcessHandler(BenchmarkIService service) {
    this.service = service;
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
    long start = System.currentTimeMillis();
    byte[] content = new DymmyData(5).getContent();

    // put
    BenchmarkProtocol.Request request = BenchmarkProtocol.Request.newBuilder()
        .setValue(ByteString.copyFrom(content))
        .build();

    // get
    service.service(request)
        .whenComplete((actual, t) -> {
          if (t == null && MD5Utils.md5(content).equals(actual.getValue().toStringUtf8())) {
            okResponse(ctx);
            if (LOGGER.isInfoEnabled()) {
              LOGGER.info("Request result:success cost:{} ms", System.currentTimeMillis() - start);
            }
          } else {
            badReponse(ctx);
            if (LOGGER.isInfoEnabled()) {
              LOGGER
                  .info("Request result:failure cost:{} ms", System.currentTimeMillis() - start, t);
            }
          }
        });
  }

  private void okResponse(ChannelHandlerContext ctx) {
    FullHttpResponse ok =
        new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled
            .copiedBuffer("OK\n", CharsetUtil.UTF_8));
    ok.headers().add(HttpHeaderNames.CONTENT_LENGTH, 3);
    ctx.writeAndFlush(ok);
  }

  private void badReponse(ChannelHandlerContext ctx) {
    FullHttpResponse error =
        new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
    error.headers().add(HttpHeaderNames.CONTENT_LENGTH, 0);
    ctx.writeAndFlush(error);
  }

}
