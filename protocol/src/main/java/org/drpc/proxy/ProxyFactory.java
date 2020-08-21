package org.drpc.proxy;

import java.lang.reflect.Proxy;

import org.drpc.Invoker;
import org.drpc.session.DrpcSession;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, invoker, null));
  }

  @SuppressWarnings("unchecked")
  public T getSessionProxy(Class<T> clazz, Invoker invoker, DrpcSession session) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, invoker, session.getSessionID()));
  }

}
