package org.dst.drpc.async;

import java.util.concurrent.CompletableFuture;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface IServer {

  String say();

  CompletableFuture<String> say(String name);

}
