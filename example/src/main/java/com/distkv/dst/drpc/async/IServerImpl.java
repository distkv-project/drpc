package com.distkv.dst.drpc.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IServerImpl implements IServer {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ExecutorService executorService = Executors.newFixedThreadPool(32);

  @Override
  public String say() {
    return "dst";
  }

  @Override
  public CompletableFuture<String> say(String name) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executorService.submit(() -> {
      sleep(3000);
      future.complete(name + "aysnc finished");
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
