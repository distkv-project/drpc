package com.distkv.drpc.pb;

import java.util.concurrent.CompletableFuture;

import com.distkv.drpc.pb.generated.StringProtocol;
import com.distkv.drpc.pb.generated.StringProtocol;

public interface IServer {

  StringProtocol.GetResponse say(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.GetResponse> asyncSay(StringProtocol.GetRequest request);

}
