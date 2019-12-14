package com.distkv.drpc.benchmark.dubbo;

import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request);
}
