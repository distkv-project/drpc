package com.distkv.drpc.pb;

import com.distkv.drpc.pb.generated.StringProtocol;

import java.util.concurrent.CompletableFuture;

public interface IPBServer {

  CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request);

}