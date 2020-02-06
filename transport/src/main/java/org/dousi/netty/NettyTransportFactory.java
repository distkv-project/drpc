package org.dousi.netty;

import org.dousi.api.Handler;
import org.dousi.api.Server;
import org.dousi.api.ServerFactory;
import org.dousi.config.ServerConfig;
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
