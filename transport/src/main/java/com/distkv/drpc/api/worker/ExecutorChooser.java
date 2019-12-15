package com.distkv.drpc.api.worker;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorChooserFactory;

public interface ExecutorChooser extends EventExecutorChooserFactory.EventExecutorChooser {

  EventExecutor next(int taskId);

}
