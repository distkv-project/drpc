package org.dst.drpc.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dst.drpc.common.URL;
import org.dst.drpc.exception.TransportException;
import org.dst.drpc.model.DrpcAddress;

public abstract class ServerFactory {

  private Map<DrpcAddress, Server> activeServer = new ConcurrentHashMap<>();


  public Server createServer(URL url, List<Handler> handlers) {
    DrpcAddress serverAddress = url.getIpPortPair();
    Server server;
    if (activeServer.containsKey(serverAddress)) {
      server = activeServer.get(serverAddress);
      if (server.isOpen()) {
        RoutableHandler routableHandler = server.getRoutableHandler();
        if (routableHandler == null) {
          throw new TransportException("Server's routableHandler can't be null");
        }

        handlers.forEach((handler) -> {
          if (handler instanceof RoutableHandler) {
            routableHandler.merge((RoutableHandler) handler);
          } else {
            routableHandler.registerHandler(handler);
          }
        });

        return server;
      } else {
        // This is the code path of disconnected server.
        activeServer.remove(serverAddress);
      }
    }

    server = doCreateServer(url, handlers);
    activeServer.put(serverAddress, server);
    return server;
  }

  protected abstract Server doCreateServer(URL url, List<Handler> handler);

}
