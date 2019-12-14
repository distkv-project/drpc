package com.distkv.drpc.benchmark.common;

import com.distkv.drpc.benchmark.common.generated.StringProtocol;
import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request);
}
