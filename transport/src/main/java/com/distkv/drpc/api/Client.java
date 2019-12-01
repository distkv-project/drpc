package com.distkv.drpc.api;

import com.distkv.drpc.api.async.Request;
import com.distkv.drpc.api.async.Response;
import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.common.URL;
import com.distkv.drpc.api.async.Request;
import com.distkv.drpc.api.async.Response;
import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.common.URL;


public interface Client {

  URL getUrl();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  Response invoke(Request request);

}
