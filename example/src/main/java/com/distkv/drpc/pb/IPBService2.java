package com.distkv.drpc.pb;

import com.distkv.drpc.pb.generated.StringProtocol;

import java.util.concurrent.CompletableFuture;

public interface IPBService2 {

  CompletableFuture<StringProtocol.GetResponse> get2(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put2(StringProtocol.PutRequest request);

}
