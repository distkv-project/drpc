package org.dousi.test.common;

import org.dousi.test.generated.BenchmarkProtocol;
import java.util.concurrent.CompletableFuture;

public interface BenchmarkIService {

  CompletableFuture<BenchmarkProtocol.Response> service(BenchmarkProtocol.Request request);

}
