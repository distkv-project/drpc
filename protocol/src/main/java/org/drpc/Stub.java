package org.drpc;

import org.drpc.proxy.ProxyFactory;
import org.drpc.api.Client;
import org.drpc.session.DrpcSession;

public class Stub<T> {

  private final Class<T> interfaceClass;

  public Stub(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public T getService(Client client) {
    Invoker<T> invoker = new DefaultInvoker<>(client, interfaceClass);
    return new ProxyFactory<T>().getProxy(interfaceClass, invoker);
  }


  public T getService(Client client, DrpcSession session) {
    Invoker<T> invoker = new DefaultInvoker<>(client, interfaceClass);
    return new ProxyFactory<T>().getSessionProxy(interfaceClass, invoker, session);
  }

}
