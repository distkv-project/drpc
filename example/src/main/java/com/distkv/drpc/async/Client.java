package com.distkv.drpc.async;

import com.distkv.drpc.config.ClientConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.LongAdder;
import com.distkv.drpc.Proxy;
import com.distkv.drpc.netty.NettyClient;

public class Client {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    com.distkv.drpc.api.Client client = new NettyClient(clientConfig);
    client.open();
    Proxy<IServer> proxy = new Proxy<>();
    proxy.setInterfaceClass(IServer.class);
    IServer service = proxy.getService(client);
    System.out.println(service.say());

    com.distkv.drpc.api.Client client2 = new NettyClient(clientConfig);
    client2.open();
    Proxy<IServer2> proxy2 = new Proxy<>();
    proxy2.setInterfaceClass(IServer2.class);
    IServer2 service1 = proxy2.getService(client2);
    System.out.println(service1.say2());

    LongAdder totalCost = new LongAdder();
    for (int i = 0; i < 1000; i++) {
      long b = System.currentTimeMillis();
      CompletableFuture<String> future = service.sayAsync("async rpc");
      future.whenComplete((r, t) -> {
        if (t != null) {
          throw new IllegalStateException(t);
        } else {
          System.out.println(r);
        }
        totalCost.add(System.currentTimeMillis() - b);
        System.out.println("cost: " + (System.currentTimeMillis() - b));
      });
    }
    System.out.println(totalCost.longValue() / 1000);


    // Start to test IServer2 with async.
    CompletableFuture<String> future = service1.say2Async("async say2");
    future.whenComplete((r, t) -> {
      System.out.println("Wow, IServer2: " + r);
    });

    client.close();
    client2.close();
  }

}
