package org.dousi.dousi.api;

import org.dousi.dousi.codec.Codec;
import org.dousi.dousi.config.ClientConfig;

public interface Client {

  ClientConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
