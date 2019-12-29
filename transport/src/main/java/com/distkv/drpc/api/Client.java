package com.distkv.drpc.api;

import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.config.ClientConfig;

public interface Client {

  ClientConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request)  throws InterruptedException;

}
