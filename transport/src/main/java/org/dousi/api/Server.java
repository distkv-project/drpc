package org.dousi.api;

import org.dousi.codec.Codec;
import org.dousi.api.worker.HashableExecutor;
import org.dousi.config.ServerConfig;

public interface Server {

  ServerConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  HashableExecutor getExecutor();

}
