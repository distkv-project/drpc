package org.dst.drpc.pb;

import java.util.concurrent.CompletableFuture;
import org.dst.drpc.pb.generated.StringProtocol;

public interface IServer {

  StringProtocol.GetResponse say(StringProtocol.GetRequest request);

  CompletableFuture<StringProtocol.GetResponse> asyncSay(StringProtocol.GetRequest request);

}
