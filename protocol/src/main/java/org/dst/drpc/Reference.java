package org.dst.drpc;

import org.dst.drpc.api.Client;
import org.dst.drpc.common.URL;
import org.dst.drpc.exception.DrpcException;
import org.dst.drpc.netty.NettyClient;
import org.dst.drpc.proxy.ProxyFactory;


public class Reference<T> {

  private Class<T> interfaceClass;

  private URL serverUrl;

  public Reference() {
    this.serverUrl = new URL();
  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public void setAddress(String address) {
    if (!address.contains("://")) {
      throw new DrpcException("Empty protocol");
    }
    String protocol = address.substring(0, address.indexOf("://"));
    serverUrl.setProtocol(protocol);
    serverUrl.setAddress(address.substring(address.indexOf("://") + "://".length()));
  }

  public T getReference() {

    Client client = new NettyClient(serverUrl);
    client.open();
    Invoker invoker = new DefaultInvoker(client, interfaceClass);
    return new ProxyFactory<T>().getProxy(interfaceClass, invoker);
  }

}
