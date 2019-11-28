package com.distkv.dst.drpc.netty;

import com.distkv.dst.drpc.api.Handler;
import com.distkv.dst.drpc.api.Server;
import com.distkv.dst.drpc.api.ServerFactory;
import com.distkv.dst.drpc.common.URL;

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
  protected Server doCreateServer(URL url, Handler handler) {
    return new NettyServer(url, handler);
  }
}
