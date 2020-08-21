package org.drpc.test.common;

import org.drpc.test.generated.BenchmarkProtocol;
import java.util.concurrent.CompletableFuture;

public interface BenchmarkIService {

  CompletableFuture<BenchmarkProtocol.Response> service(BenchmarkProtocol.Request request);

}
