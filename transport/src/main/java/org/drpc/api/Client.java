package org.drpc.api;

import org.drpc.codec.Codec;
import org.drpc.config.ClientConfig;

public interface Client {

  ClientConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
