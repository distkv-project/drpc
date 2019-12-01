package com.distkv.drpc;

import com.distkv.drpc.proxy.ProxyFactory;
import com.distkv.drpc.api.Client;
import com.distkv.drpc.common.URL;
import com.distkv.drpc.exception.DrpcException;
import com.distkv.drpc.netty.NettyClient;


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
