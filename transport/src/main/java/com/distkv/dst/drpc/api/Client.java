package com.distkv.dst.drpc.api;

import com.distkv.dst.drpc.api.async.Request;
import com.distkv.dst.drpc.api.async.Response;
import com.distkv.dst.drpc.codec.Codec;
import com.distkv.dst.drpc.common.URL;


public interface Client {

  URL getUrl();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
