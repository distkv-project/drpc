package com.distkv.drpc.api;

import com.distkv.drpc.api.worker.HashableExecutor;
import com.distkv.drpc.config.ServerConfig;
import com.distkv.drpc.codec.Codec;

public interface Server {

  ServerConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  HashableExecutor getExecutor();

}
