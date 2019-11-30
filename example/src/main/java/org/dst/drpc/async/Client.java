package org.dst.drpc.async;


import java.sql.Ref;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;
import org.dst.drpc.Reference;


public class Client {

  public static void main(String[] args) {
    Reference<IServer> reference = new Reference<>();
    reference.setAddress("dst://127.0.0.1:8080");
    reference.setInterfaceClass(IServer.class);

    IServer server = reference.getReference();
    System.out.println(server.say());

    Reference<IServer2> reference2 = new Reference<>();
    reference2.setAddress("dst://127.0.0.1:8080");
    reference2.setInterfaceClass(IServer2.class);
    IServer2 server2 = reference2.getReference();
    System.out.println(server2.say2());

    LongAdder totalCost = new LongAdder();
    for(int i = 0;i<1000;i++) {
      long b = System.currentTimeMillis();
      CompletableFuture<String> future = server.say("async rpc");
      future.whenComplete((r, t) -> {
        if(t != null) {
          throw new IllegalStateException(t);
        } else {
          System.out.println(r);
        }
        totalCost.add(System.currentTimeMillis() - b);
        System.out.println("cost: " + (System.currentTimeMillis() - b));
      });
    }
    System.out.println(totalCost.longValue() / 1000);

  }

}
