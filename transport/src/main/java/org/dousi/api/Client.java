package org.dousi.api;

import org.dousi.codec.Codec;
import org.dousi.config.ClientConfig;

public interface Client {

  ClientConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
