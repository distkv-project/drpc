package com.distkv.drpc.proxy;

import java.lang.reflect.Proxy;
import com.distkv.drpc.Invoker;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, invoker));
  }

}
