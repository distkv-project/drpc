package com.distkv.drpc.netty;

import com.distkv.drpc.api.Handler;
import com.distkv.drpc.api.Server;
import com.distkv.drpc.api.ServerFactory;

import com.distkv.drpc.config.ServerConfig;
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
