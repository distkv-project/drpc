package com.distkv.drpc.api.worker;

public interface TaskHashedExecutor {

  void submit(int taskId, Runnable task);

}
