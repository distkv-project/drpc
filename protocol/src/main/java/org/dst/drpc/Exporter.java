package org.dst.drpc;

import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.config.ServerConfig;
import org.dst.drpc.netty.NettyTransportFactory;
import java.util.concurrent.CopyOnWriteArrayList;


public class Exporter {

  /**
   * The service handlers.
   * Note that CopyOnWriteArrayList might not be efficient too much when writing,
   * but this is not a writing-bundle collection.
   */
  private CopyOnWriteArrayList<Handler> serviceHandlers = new CopyOnWriteArrayList<>();

  private ServerConfig serverConfig;

  public Exporter(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
    serverConfig.setReadOnly();
  }

  /**
   * Register the service into this exporter.
   *
   * @param interfaceClass The interface that we want to export.
   * @param serviceObject The object that this service implementation.
   */
  public <T> void registerService(Class<T> interfaceClass, T serviceObject) {
    serviceHandlers.add(new HandlerDelegate(new ServerImpl<T>(serviceObject, interfaceClass)));
  }

  public void export() {
    Server server = NettyTransportFactory.getInstance().createServer(serverConfig, serviceHandlers);
    server.open();
  }
}
