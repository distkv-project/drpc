package org.drpc.pb;

import org.drpc.session.DrpcSession;
import org.drpc.Proxy;
import org.drpc.api.Client;
import org.drpc.config.ClientConfig;
import org.drpc.netty.DrpcClient;
import org.drpc.pb.generated.StringProtocol;

import java.util.concurrent.CompletableFuture;

public class ExampleClient {

  public static void main(String[] args) throws Throwable {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    Client client = new DrpcClient(clientConfig);
    client.open();

    Proxy<ExampleService> proxy = new Proxy<>();
    proxy.setInterfaceClass(ExampleService.class);
    ExampleService service = proxy.getService(client);

    StringProtocol.PutRequest putRequest = StringProtocol.PutRequest.newBuilder()
        .setKey("dstPut")
        .setValue("PutValue")
        .build();

    StringProtocol.GetRequest getRequest = StringProtocol.GetRequest.newBuilder()
        .setKey("dstGet").build();

    //sync
    StringProtocol.GetResponse getResponse = service.get(getRequest).get();
    System.out.println(getResponse.getStatus());
    System.out.println(getResponse.getValue());
    StringProtocol.PutResponse putResponse = service.put(putRequest).get();
    System.out.println(putResponse.getStatus());

    //async
    CompletableFuture future1 = service.get(getRequest);
    future1.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(getResponse.getStatus());
        System.out.println(getResponse.getValue());
      }
    });

    CompletableFuture future2 = service.put(putRequest);
    future2.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(putResponse.getStatus());
      }
    });


    //session (keep order)
    DrpcSession session = DrpcSession.createSession();
    ExampleService sessionService = proxy.getService(client, session);

    //async (keep order in server)
    CompletableFuture sessionFuture1 = sessionService.get(getRequest);
    sessionFuture1.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(getResponse.getValue());
      }
    });
    CompletableFuture sessionFuture2 = sessionService.put(putRequest);
    sessionFuture2.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(putResponse.getStatus());
      }
    });

    sessionFuture1.get();
    sessionFuture2.get();

    client.close();
  }
}
