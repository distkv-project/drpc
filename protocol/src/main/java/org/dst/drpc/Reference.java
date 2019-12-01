package org.dst.drpc;

import org.dst.drpc.api.Client;
import org.dst.drpc.config.ClientConfig;
import org.dst.drpc.netty.NettyClient;
import org.dst.drpc.proxy.ProxyFactory;


public class Reference<T> {

  private Class<T> interfaceClass;

  private ClientConfig clientConfig;

  public Reference(ClientConfig clientConfig) {
    this.clientConfig = clientConfig;
    clientConfig.setReadOnly();
  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public T getReference() {

    Client client = new NettyClient(clientConfig);
    client.open();
    Invoker invoker = new DefaultInvoker(client, interfaceClass);
    return new ProxyFactory<T>().getProxy(interfaceClass, invoker);
  }

}
