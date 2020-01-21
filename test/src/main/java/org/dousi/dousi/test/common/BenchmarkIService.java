package org.dousi.dousi.test.common;

import com.distkv.drpc.test.generated.BenchmarkProtocol;
import java.util.concurrent.CompletableFuture;

public interface BenchmarkIService {

  CompletableFuture<BenchmarkProtocol.Response> service(BenchmarkProtocol.Request request);

}
