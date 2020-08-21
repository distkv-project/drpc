package org.drpc.api;

import org.drpc.codec.Codec;
import org.drpc.api.worker.HashableExecutor;
import org.drpc.config.ServerConfig;

public interface Server {

  ServerConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  HashableExecutor getExecutor();

}
