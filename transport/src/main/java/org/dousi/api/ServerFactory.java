package org.dousi.api;

import org.dousi.common.DrpcAddress;
import org.dousi.exception.TransportException;
import org.dousi.config.ServerConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ServerFactory {

  private Map<DrpcAddress, Server> activeServer = new ConcurrentHashMap<>();


  public Server createServer(ServerConfig serverConfig, List<Handler> handlers) {
    DrpcAddress serverAddress = serverConfig.getDrpcAddress();
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

    server = doCreateServer(serverConfig, handlers);
    activeServer.put(serverAddress, server);
    return server;
  }

  protected abstract Server doCreateServer(ServerConfig serverConfig, List<Handler> handler);

}
