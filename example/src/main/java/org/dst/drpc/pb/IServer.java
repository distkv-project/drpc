package org.dst.drpc.pb;

import java.util.concurrent.CompletableFuture;


public interface IServer {

  StringProtocol.GetResponse say(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.GetResponse> asyncSay(StringProtocol.GetRequest request);

}
