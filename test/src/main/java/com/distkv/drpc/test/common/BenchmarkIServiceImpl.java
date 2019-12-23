package com.distkv.drpc.test.common;

import com.distkv.drpc.test.generated.BenchmarkProtocol;
import com.google.protobuf.ByteString;
import java.util.concurrent.CompletableFuture;

public class BenchmarkIServiceImpl implements BenchmarkIService {

  @Override
  public CompletableFuture<BenchmarkProtocol.Response> service(
      BenchmarkProtocol.Request request) {
    CompletableFuture<BenchmarkProtocol.Response> future = new CompletableFuture<>();
    BenchmarkProtocol.Response response = BenchmarkProtocol.Response.newBuilder()
        .setValue(ByteString.copyFromUtf8(MD5Utils.md5(request.getValue().toByteArray())))
        .build();
    future.complete(response);

    return future;
  }

}
