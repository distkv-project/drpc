package org.dousi.codec;

import org.dousi.exception.DousiCodecException;


public interface Codec {

  byte[] encode(Object message) throws DousiCodecException;

  Object decode(byte[] data, DataTypeEnum dataTypeEnum) throws DousiCodecException;

  enum DataTypeEnum {
    REQUEST,
    RESPONSE,
    ;
  }

}
