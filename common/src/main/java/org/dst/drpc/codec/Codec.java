package org.dst.drpc.codec;

import org.dst.drpc.exception.CodecException;


public interface Codec {

  byte[] encode(Object message) throws CodecException;

  Object decode(byte[] data) throws CodecException;

}
