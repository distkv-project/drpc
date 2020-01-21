package org.dousi.test.common;

import org.dousi.test.generated.EchoProtocol;
import java.util.concurrent.CompletableFuture;

public class IServiceImpl implements IService {

  private volatile int value;

  @Override
  public CompletableFuture<EchoProtocol.GetResponse> get() {
    CompletableFuture<EchoProtocol.GetResponse> future = new CompletableFuture<>();
    EchoProtocol.GetResponse response = EchoProtocol.GetResponse.newBuilder()
        .setValue(value)
        .build();
    future.complete(response);
    return future;
  }

  @Override
  public void put(EchoProtocol.PutRequest request) {
    value = request.getValue();
  }
}
