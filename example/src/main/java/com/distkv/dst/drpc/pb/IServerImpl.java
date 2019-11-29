package com.distkv.dst.drpc.pb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.distkv.dst.drpc.pb.generated.StringProtocol;
import com.distkv.dst.drpc.pb.generated.CommonProtocol.Status;

public class IServerImpl implements IServer {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ExecutorService executorService = Executors.newFixedThreadPool(32);

  @Override
  public StringProtocol.GetResponse say(StringProtocol.GetRequest request) {
    StringProtocol.GetResponse response = StringProtocol.GetResponse.newBuilder()
        .setValue("key: "+ request.getKey())
        .setStatus(Status.OK)
        .build();

    return response;
  }

  @Override
  public CompletableFuture<StringProtocol.GetResponse> asyncSay(StringProtocol.GetRequest request) {
    CompletableFuture<StringProtocol.GetResponse> future = new CompletableFuture<>();
    StringProtocol.GetResponse response = StringProtocol.GetResponse.newBuilder()
        .setValue(request.getKey() + "aysnc finished")
        .setStatus(Status.OK)
        .build();

    executorService.submit(() -> {
      sleep(3000);
      future.complete(response);
    });
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
