package com.distkv.drpc.api.worker;

import io.netty.util.concurrent.EventExecutorGroup;

public interface HashableExecutor extends EventExecutorGroup {

  void submit(int taskId, Runnable task);

}
