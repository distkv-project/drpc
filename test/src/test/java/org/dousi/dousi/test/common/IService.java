package org.dousi.dousi.test.common;

import com.distkv.drpc.test.generated.EchoProtocol;
import java.util.concurrent.CompletableFuture;

public interface IService {

  CompletableFuture<EchoProtocol.GetResponse> get();

  void put(EchoProtocol.PutRequest request);

}
