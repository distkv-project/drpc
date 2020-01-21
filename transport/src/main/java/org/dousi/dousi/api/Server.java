package org.dousi.dousi.api;

import org.dousi.dousi.codec.Codec;
import org.dousi.dousi.api.worker.HashableExecutor;
import org.dousi.dousi.config.ServerConfig;

public interface Server {

  ServerConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  HashableExecutor getExecutor();

}
