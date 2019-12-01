package com.distkv.drpc.netty;

import com.distkv.drpc.common.URL;
import com.distkv.drpc.api.Handler;
import com.distkv.drpc.api.Server;
import com.distkv.drpc.api.ServerFactory;

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
  protected Server doCreateServer(URL url, List<Handler> handlers) {
    return new NettyServer(url, handlers);
  }
}
