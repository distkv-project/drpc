package org.dst.drpc.netty;

import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.api.ServerFactory;
import org.dst.drpc.common.URL;

/**
 * NettyFactory实现
 */
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
