package org.dousi.pb;

import org.dousi.pb.generated.StringProtocol;
import java.util.concurrent.CompletableFuture;

public interface ExampleService {

  CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request);

}
