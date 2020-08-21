package org.drpc.netty;

import org.drpc.api.Handler;
import org.drpc.api.Server;
import org.drpc.api.ServerFactory;
import org.drpc.config.ServerConfig;
import java.util.List;

public class NettyTransportFactory extends ServerFactory {

  private NettyTransportFactory() {
  }

  private static class InstanceHolder {

    private static ServerFactory factory = new NettyTransportFactory();
  }

  public static ServerFactory getInstance() {
    return InstanceHolder.factory;
  }

  @Override
  protected Server doCreateServer(ServerConfig serverConfig, List<Handler> handlers) {
    return new NettyServer(serverConfig, handlers);
  }
}
