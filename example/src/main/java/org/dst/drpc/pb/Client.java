package org.dst.drpc.pb;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;
import org.dst.drpc.Reference;
import org.dst.drpc.config.ClientConfig;
import org.dst.drpc.pb.generated.StringProtocol;


public class Client {

  public static void main(String[] args) throws Throwable {
    ClientConfig clientConfig = new ClientConfig();

    Reference<IServer> reference = new Reference<>(clientConfig);
    reference.setInterfaceClass(IServer.class);

    IServer server = reference.getReference();

    org.dst.drpc.pb.generated.StringProtocol.GetRequest request = StringProtocol.GetRequest.newBuilder()
        .setKey("dst").build();
    System.out.println(server.say(request).getValue());

    LongAdder totalCost = new LongAdder();
    for(int i = 0;i<1000;i++) {
      long b = System.currentTimeMillis();
      CompletableFuture<StringProtocol.GetResponse> future = server.asyncSay(request);
      future.whenComplete((r, t) -> {
        if(t != null) {
          throw new IllegalStateException(t);
        } else {
          System.out.println(r.getValue());
        }
        totalCost.add(System.currentTimeMillis() - b);
        System.out.println("cost: " + (System.currentTimeMillis() - b));
      });
    }
    System.out.println(totalCost.longValue() / 1000);

  }

}
