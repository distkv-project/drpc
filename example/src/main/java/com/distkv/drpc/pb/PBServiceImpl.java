package com.distkv.drpc.pb;

import com.distkv.drpc.pb.generated.CommonProtocol;
import com.distkv.drpc.pb.generated.StringProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PBServiceImpl implements IPBService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private ExecutorService executorService = Executors.newFixedThreadPool(32);

  @Override
  public CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request) {
    CompletableFuture<StringProtocol.GetResponse> future = new CompletableFuture<>();
    StringProtocol.GetResponse response = StringProtocol.GetResponse.newBuilder()
          .setValue(request.getKey() + " get() request success")
          .setStatus(CommonProtocol.Status.OK)
          .build();
    //exec sth delayed async
    executorService.submit(() -> {
      sleep(3000);
      future.complete(response);
    });
    System.out.println("server receive: " + request.getKey());
    return future;
  }

  @Override
  public CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request) {
    CompletableFuture<StringProtocol.PutResponse> future = new CompletableFuture<>();
    StringProtocol.PutResponse response = StringProtocol.PutResponse.newBuilder()
          .setStatus(CommonProtocol.Status.OK)
          .build();
    //exec sth delayed async
    executorService.submit(() -> {
      sleep(3000);
      future.complete(response);
    });
    System.out.println("server receive key: " + request.getKey());
    System.out.println("server receive value: " + request.getValue());
    return future;
  }

  private void sleep(long t) {
    try {
      Thread.sleep(t);
    } catch (Exception e) {
      logger.error("exception", e);
    }
  }
}
