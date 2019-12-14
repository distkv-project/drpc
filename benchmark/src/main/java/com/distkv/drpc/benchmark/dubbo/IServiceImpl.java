package com.distkv.drpc.benchmark.dubbo;

import com.distkv.drpc.benchmark.dubbo.StringProtocol.GetRequest;
import com.distkv.drpc.benchmark.dubbo.StringProtocol.GetResponse;
import com.distkv.drpc.benchmark.dubbo.StringProtocol.PutRequest;
import com.distkv.drpc.benchmark.dubbo.StringProtocol.PutResponse;
import com.distkv.drpc.benchmark.dubbo.StringProtocol.Status;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IServiceImpl implements IService {

  private Map<String, String> map = new ConcurrentHashMap<>();

  @Override
  public CompletableFuture<GetResponse> get(GetRequest request) {
    CompletableFuture<GetResponse> future = new CompletableFuture<>();
    future.complete(GetResponse.builder()
        .status(Status.OK)
        .value(map.get(request.getKey()))
        .build());
    return future;
  }

  @Override
  public CompletableFuture<PutResponse> put(PutRequest request) {
    map.put(request.getKey(), request.getValue());
    CompletableFuture<PutResponse> future = new CompletableFuture<>();
    future.complete(PutResponse.builder()
        .status(Status.OK)
        .build());
    return future;
  }
}
