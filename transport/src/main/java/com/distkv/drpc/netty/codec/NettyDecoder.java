package com.distkv.drpc.netty.codec;

import com.distkv.drpc.constants.CodecConstants;
import com.distkv.drpc.exception.TransportException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import com.distkv.drpc.constants.CodecConstants;
import com.distkv.drpc.exception.TransportException;

public class NettyDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() <= CodecConstants.HEADER_LENGTH) {
      return;
    }

    in.markReaderIndex();
    short magic = in.readShort();
    if (magic != CodecConstants.MAGIC_HEAD) {
      in.resetReaderIndex();
      throw new TransportException("NettyDecoder: magic number error: " + magic);
    }

    in.skipBytes(2);
    int contentLength = in.readInt();
    if (in.readableBytes() < contentLength + 8/* requestId 8 byte */) {
      in.resetReaderIndex();
      return;
    }

    in.resetReaderIndex();
    byte[] data = new byte[CodecConstants.HEADER_LENGTH + contentLength];
    in.readBytes(data);
    out.add(data);
  }
}
