package org.dst.drpc.pb;

import org.dst.drpc.pb.generated.StringProtocol;

import java.util.concurrent.CompletableFuture;

public interface IPBServer2 {

  CompletableFuture<StringProtocol.GetResponse> get2(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put2(StringProtocol.PutRequest request);

}
