package com.distkv.drpc.benchmark.drpc;

import com.distkv.drpc.Proxy;
import com.distkv.drpc.api.Client;
import com.distkv.drpc.benchmark.common.IService;
import com.distkv.drpc.benchmark.common.consumer.Consumer;
import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.netty.NettyClient;

public class DrpcConsumer {

  public static void main(String[] args) {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:25500")
        .build();
    Client client = new NettyClient(clientConfig);
    client.open();

    Proxy<IService> proxy = new Proxy<>();
    proxy.setInterfaceClass(IService.class);
    IService service = proxy.getService(client);

    // benchmark gateway
    Consumer server = new Consumer();
    server.start(service);
  }
}
