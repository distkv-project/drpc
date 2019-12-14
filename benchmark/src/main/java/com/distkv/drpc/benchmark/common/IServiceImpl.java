package com.distkv.drpc.benchmark.common;

import com.distkv.drpc.benchmark.common.generated.CommonProtocol.Status;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.GetRequest;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.GetResponse;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.PutRequest;
import com.distkv.drpc.benchmark.common.generated.StringProtocol.PutResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IServiceImpl implements IService {

  private Map<String, String> map = new ConcurrentHashMap<>();

  @Override
  public CompletableFuture<GetResponse> get(GetRequest request) {
    CompletableFuture<GetResponse> future = new CompletableFuture<>();
    future.complete(GetResponse.newBuilder()
        .setStatus(Status.OK)
        .setValue(map.get(request.getKey()))
        .build());
    return future;
  }

  @Override
  public CompletableFuture<PutResponse> put(PutRequest request) {
    map.put(request.getKey(), request.getValue());
    CompletableFuture<PutResponse> future = new CompletableFuture<>();
    future.complete(PutResponse.newBuilder()
        .setStatus(Status.OK)
        .build());
    return future;
  }
}
