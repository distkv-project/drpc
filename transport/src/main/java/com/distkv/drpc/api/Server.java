package com.distkv.drpc.api;

import java.util.concurrent.Executor;

import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.common.URL;
import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.common.URL;

public interface Server {

  URL getUrl();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  Executor getExecutor();

}
