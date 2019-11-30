package org.dst.drpc.pb;

import org.dst.drpc.pb.generated.CommonProtocol;
import org.dst.drpc.pb.generated.StringProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PBServerImpl2 implements IPBServer2 {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private ExecutorService executorService = Executors.newFixedThreadPool(32);

  @Override
  public CompletableFuture<StringProtocol.GetResponse> get2(StringProtocol.GetRequest request) {
    CompletableFuture<StringProtocol.GetResponse> future = new CompletableFuture<>();
    StringProtocol.GetResponse response = StringProtocol.GetResponse.newBuilder()
          .setValue(request.getKey() + " get() request2 success")
          .setStatus(CommonProtocol.Status.OK)
          .build();
    //exec sth delayed async
    executorService.submit(() -> {
      sleep(3000);
      future.complete(response);
    });
    System.out.println("server2 receive: "+request.getKey());
    return future;
  }

  @Override
  public CompletableFuture<StringProtocol.PutResponse> put2(StringProtocol.PutRequest request) {
    CompletableFuture<StringProtocol.PutResponse> future = new CompletableFuture<>();
    StringProtocol.PutResponse response = StringProtocol.PutResponse.newBuilder()
          .setStatus(CommonProtocol.Status.OK)
          .build();
    //exec sth delayed async
    executorService.submit(() -> {
      sleep(3000);
      future.complete(response);
    });
    System.out.println("server2 receive key: "+request.getKey());
    System.out.println("server2 receive value: "+request.getValue());
    return future;
  }

  private void sleep(long t) {
    try {
      Thread.sleep(t);
    } catch (Exception e) {
      logger.error("exception2", e);
    }
  }
}
