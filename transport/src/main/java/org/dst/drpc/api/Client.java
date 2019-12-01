package org.dst.drpc.api;

import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.codec.Codec;
import org.dst.drpc.config.ClientConfig;


public interface Client {

  ClientConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
