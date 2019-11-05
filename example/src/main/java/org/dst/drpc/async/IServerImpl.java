package org.dst.drpc.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class IServerImpl implements IServer {

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
      // ignore
    }
  }
}
