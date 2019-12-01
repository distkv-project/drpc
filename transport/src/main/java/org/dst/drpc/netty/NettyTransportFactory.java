package org.dst.drpc.netty;

import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.api.ServerFactory;

import java.util.List;
import org.dst.drpc.config.ServerConfig;

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
