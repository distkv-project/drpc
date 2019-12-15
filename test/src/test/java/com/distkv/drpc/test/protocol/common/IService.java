package com.distkv.drpc.test.protocol.common;

import com.distkv.drpc.test.protocol.generated.EchoProtocol;
import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<EchoProtocol.GetResponse> get();

  void put(EchoProtocol.PutRequest request);

}
