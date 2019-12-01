package com.distkv.drpc.codec;

import com.distkv.drpc.exception.CodecException;
import com.distkv.drpc.exception.CodecException;


public interface Codec {

  byte[] encode(Object message) throws CodecException;

  Object decode(byte[] data) throws CodecException;

}
