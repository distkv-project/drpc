package org.dst.drpc.api;

import java.util.concurrent.Executor;
import org.dst.drpc.codec.Codec;
import org.dst.drpc.common.URL;

public interface Server {

  URL getUrl();

  void open();

  boolean isOpen();

  void close();

  Codec getCodec();

  RoutableHandler getRoutableHandler();

  Executor getExecutor();

}
