package org.dousi;

import org.dousi.proxy.ProxyFactory;
import org.dousi.api.Client;

public class Proxy<T> {

  private Class<T> interfaceClass;

  public Proxy() {

  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public T getService(Client client) {
    Invoker invoker = new DefaultInvoker(client, interfaceClass);
    return new ProxyFactory<T>().getProxy(interfaceClass, invoker);
  }

}
