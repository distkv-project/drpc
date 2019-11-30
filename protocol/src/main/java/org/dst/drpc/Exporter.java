package org.dst.drpc;


import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.common.URL;
import org.dst.drpc.netty.NettyTransportFactory;
import org.dst.drpc.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class Exporter {

  /**
   * The reference objects of services. The key is the string of the
   * service name and the value is the service object.
   */
  private ConcurrentHashMap<String, Object> serviceObjects = new ConcurrentHashMap<>();

  /**
   * The classes of service interfaces. The key is the string of the
   * service name and the value is the class of the service interface.
   *
   * TODO(qwang): I think this should be removed because we can get
   * the interface classes by `class.forName`.
   */
  private ConcurrentHashMap<String, Class<?>> serviceInterfaceClasses = new ConcurrentHashMap<>();

  private ConcurrentHashMap<String, Handler> serviceHandlers = new ConcurrentHashMap<>();

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
    serviceHandlers.put(interfaceClass.getName(), new HandlerDelegate(new ServerImpl<T>(serviceObject, interfaceClass)));
    serviceInterfaceClasses.put(interfaceClass.getName(), interfaceClass);
    serviceObjects.put(interfaceClass.getName(), serviceObject);
  }

  public void setPort(int port) {
    serverUrl.setPort(port);
  }

  public void export() {
    List<Handler> handlers = new ArrayList<>(serviceInterfaceClasses.size());
    serviceHandlers.forEach((key, value) -> handlers.add(value));
    Server server = NettyTransportFactory.getInstance().createServer(serverUrl, handlers);
    server.open();
  }
}
