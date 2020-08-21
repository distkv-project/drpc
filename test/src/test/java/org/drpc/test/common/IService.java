package org.drpc.test.common;

import org.drpc.test.generated.EchoProtocol;
import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<EchoProtocol.GetResponse> get();

  void put(EchoProtocol.PutRequest request);

}
