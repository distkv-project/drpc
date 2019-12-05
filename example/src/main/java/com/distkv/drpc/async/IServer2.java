package com.distkv.drpc.async;

import java.util.concurrent.CompletableFuture;

public interface IServer2 {

  String say2();

  CompletableFuture<String> say2Async(String name);
}
