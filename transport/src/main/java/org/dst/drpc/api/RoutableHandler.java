package org.dst.drpc.api;

import java.util.List;

public interface RoutableHandler extends Handler {

  void registerHandler(Handler handler);

  void merge(RoutableHandler handler);

  List<Handler> getAllHandler();

  @Override
  Object handle(Object message);

  @Override
  default String getServerName() {
    throw new UnsupportedOperationException();
  }

}
