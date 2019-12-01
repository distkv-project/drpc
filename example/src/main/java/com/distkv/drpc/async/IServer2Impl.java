package com.distkv.drpc.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IServer2Impl implements IServer2 {

  private ExecutorService threadPool = Executors.newFixedThreadPool(2);

  @Override
  public String say2() {
    return "This is IServer2.";
  }

  @Override
  public CompletableFuture<String> say2(String name) {
    CompletableFuture<String> future = new CompletableFuture<>();
    threadPool.submit(() -> {
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        future.complete("Failed to Async_Say2:" + name);
      }
      future.complete("Async_Say2:" + name);
    });

    return future;
  }
}
