package org.dst.drpc.netty;

import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.api.ServerFactory;
import org.dst.drpc.common.URL;

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
