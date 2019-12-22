package com.distkv.drpc.pb;

import com.distkv.drpc.pb.generated.CommonProtocol;
import com.distkv.drpc.pb.generated.StringProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PBServiceImpl implements IPBService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public CompletableFuture<StringProtocol.GetResponse> get(StringProtocol.GetRequest request) {
    CompletableFuture<StringProtocol.GetResponse> future = new CompletableFuture<>();
    StringProtocol.GetResponse response = StringProtocol.GetResponse.newBuilder()
          .setValue(request.getKey() + " get() request success")
          .setStatus(CommonProtocol.Status.OK)
          .build();
    future.complete(response);
    logger.info("server receive: " + request.getKey());
    return future;
  }

  @Override
  public CompletableFuture<StringProtocol.PutResponse> put(StringProtocol.PutRequest request) {
    CompletableFuture<StringProtocol.PutResponse> future = new CompletableFuture<>();
    StringProtocol.PutResponse response = StringProtocol.PutResponse.newBuilder()
          .setStatus(CommonProtocol.Status.OK)
          .build();
    future.complete(response);
    logger.info("server receive key: " + request.getKey());
    logger.info("server receive value: " + request.getValue());
    return future;
  }
}
