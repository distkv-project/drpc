package org.dst.drpc.api;

import java.util.List;
import org.dst.drpc.common.URL;

public interface RoutableHandler extends Handler {

  Handler getHandlerByServerName(URL url);

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
