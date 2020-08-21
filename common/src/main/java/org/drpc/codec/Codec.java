package org.drpc.codec;

import org.drpc.exception.DrpcCodecException;


public interface Codec {

  byte[] encode(Object message) throws DrpcCodecException;

  Object decode(byte[] data, DataTypeEnum dataTypeEnum) throws DrpcCodecException;

  enum DataTypeEnum {
    REQUEST,
    RESPONSE,
    ;
  }

}
