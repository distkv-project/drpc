package org.dousi.test.common;

import org.dousi.test.generated.EchoProtocol;

import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<EchoProtocol.GetResponse> get();

  void put(EchoProtocol.PutRequest request);

}
