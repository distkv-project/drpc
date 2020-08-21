package org.drpc;

import org.drpc.api.Handler;
import org.drpc.api.Server;
import org.drpc.config.ServerConfig;
import org.drpc.netty.NettyTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The class that represents a drpc server.
 */
public class DrpcServer {

  private static Logger logger = LoggerFactory.getLogger(DrpcServer.class);

  private Server server;
  /**
   * The service handlers.
   * Note that CopyOnWriteArrayList might not be efficient too much when writing,
   * but this is not a writing-bundle collection.
   */
  private CopyOnWriteArrayList<Handler> serviceHandlers = new CopyOnWriteArrayList<>();

  private ServerConfig serverConfig;

  public DrpcServer(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
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

  public void run() {
    if (server != null) {
      logger.debug("server is running");
    }
    server = NettyTransportFactory.getInstance().createServer(serverConfig, serviceHandlers);
    server.open();
  }

  public void stop() {
    if (server != null) {
      server.close();
    }
  }
}
