package com.distkv.drpc;

import com.distkv.drpc.api.Handler;
import com.distkv.drpc.api.Server;
import com.distkv.drpc.common.URL;
import com.distkv.drpc.netty.NettyTransportFactory;
import com.distkv.drpc.utils.NetUtils;
import java.util.concurrent.CopyOnWriteArrayList;


public class Exporter {

  /**
   * The service handlers.
   * Note that CopyOnWriteArrayList might not be efficient too much when writing,
   * but this is not a writing-bundle collection.
   */
  private CopyOnWriteArrayList<Handler> serviceHandlers = new CopyOnWriteArrayList<>();

  URL serverUrl;

  public Exporter() {
    serverUrl = new URL();
    String localAddress = NetUtils.getLocalAddress().getHostAddress();
    serverUrl.setHost(localAddress);
  }

  public void isLocal(boolean isLocal) {
    if (isLocal) {
      serverUrl.setHost("127.0.0.1");
    }
  }

  public void setProtocol(String protocol) {
    serverUrl.setProtocol(protocol);
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

  public void setPort(int port) {
    serverUrl.setPort(port);
  }

  public void export() {
    Server server = NettyTransportFactory.getInstance().createServer(serverUrl, serviceHandlers);
    server.open();
  }
}
