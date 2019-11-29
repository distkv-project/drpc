package com.distkv.dst.drpc.codec;

import com.distkv.dst.drpc.exception.CodecException;


public interface Codec {

  byte[] encode(Object message) throws CodecException;

  Object decode(byte[] data) throws CodecException;

}
