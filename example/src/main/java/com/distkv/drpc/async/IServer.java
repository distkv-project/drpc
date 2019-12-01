package com.distkv.drpc.async;

import java.util.concurrent.CompletableFuture;


public interface IServer {

  String say();

  CompletableFuture<String> say(String name);

}
