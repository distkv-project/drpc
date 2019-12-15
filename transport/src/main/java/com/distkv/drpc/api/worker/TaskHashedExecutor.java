package com.distkv.drpc.api.worker;

import io.netty.util.concurrent.EventExecutorGroup;

public interface TaskHashedExecutor extends EventExecutorGroup {

  void submit(int taskId, Runnable task);

}
