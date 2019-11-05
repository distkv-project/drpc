package org.dst.drpc.proxy;

import java.lang.reflect.Proxy;
import org.dst.drpc.Invoker;
import org.dst.drpc.common.URL;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, URL url, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, url, invoker));
  }

}
