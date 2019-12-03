package com.distkv.drpc;

import com.distkv.drpc.proxy.ProxyFactory;
import com.distkv.drpc.api.Client;

public class Proxy<T> {

  private Class<T> interfaceClass;

  public Proxy() {

  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public T proxyClient(Client client) {
    Invoker invoker = new DefaultInvoker(client, interfaceClass);
    return new ProxyFactory<T>().getProxy(interfaceClass, invoker);
  }

}
