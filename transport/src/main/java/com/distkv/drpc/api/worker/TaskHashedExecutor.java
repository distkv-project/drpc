package com.distkv.drpc.api.worker;

/**
 * @author zrj CreateDate: 2019/11/20
 */
public interface TaskHashedExecutor {

  void submit(int taskId, Runnable task);

}
