package org.dst.drpc.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dst.drpc.common.URL;
import org.dst.drpc.exception.TransportException;
import org.dst.drpc.model.DrpcAddress;

public abstract class ServerFactory {

  private Map<DrpcAddress, Server> activeServer = new ConcurrentHashMap<>();


  public Server createServer(URL url, Handler handler) {
    DrpcAddress serverAddress = url.getIpPortPair();
    Server server;
    if (activeServer.containsKey(serverAddress)) {
      server = activeServer.get(serverAddress);
      if (server.isOpen()) {
        RoutableHandler routableHandler = server.getRoutableHandler();
        if (routableHandler == null) {
          throw new TransportException("Server's routableHandler can't be null");
        }
        if (handler instanceof RoutableHandler) {
          routableHandler.merge((RoutableHandler) handler);
        } else {
          routableHandler.registerHandler(handler);
        }
        return server;
      } else {
        // 服务已经关闭了，从map中移除，然后新建一个Server存进map
        activeServer.remove(serverAddress);
      }
    }

    // 在URL请求的ip&port的地址上面没有服务，则创建一个服务
    server = doCreateServer(url, handler);
    // 这里不要open server，工厂除了创建一个新的Server以外不应该干涉Server的生命周期
    activeServer.put(serverAddress, server);
    return server;
  }

  protected abstract Server doCreateServer(URL url, Handler handler);

}
