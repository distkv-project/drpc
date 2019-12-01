package org.dst.drpc.api;

import java.util.concurrent.Executor;
import org.dst.drpc.codec.Codec;
import org.dst.drpc.config.ServerConfig;

public interface Server {

  ServerConfig getConfig();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  Executor getExecutor();

}
