package org.dst.drpc.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<byte[]> {

  @Override
  protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
    out.writeBytes(msg);
  }
}
