package com.distkv.drpc;

import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.proxy.ProxyFactory;
import com.distkv.drpc.api.Client;
import com.distkv.drpc.netty.NettyClient;


public class Reference<T> {

  private Class<T> interfaceClass;

  private ClientConfig clientConfig;

  public Reference(ClientConfig clientConfig) {
    this.clientConfig = clientConfig;
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
