package org.drpc.pb;

import org.drpc.pb.generated.StringProtocol;
import java.util.concurrent.CompletableFuture;

public interface ExampleService {

  CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request);

}
