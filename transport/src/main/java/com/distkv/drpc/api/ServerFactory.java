package com.distkv.drpc.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.distkv.drpc.common.URL;
import com.distkv.drpc.exception.TransportException;
import com.distkv.drpc.model.DrpcAddress;

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

    // 在URL请求的ip&port的地址上面没有服务，则创建一个服务
    server = doCreateServer(url, handlers);
    // 这里不要open server，工厂除了创建一个新的Server以外不应该干涉Server的生命周期
    activeServer.put(serverAddress, server);
    return server;
  }

  protected abstract Server doCreateServer(URL url, List<Handler> handler);

}
