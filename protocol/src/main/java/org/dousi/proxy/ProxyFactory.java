package org.dousi.proxy;

import java.lang.reflect.Proxy;

import org.dousi.Invoker;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, invoker));
  }

}
