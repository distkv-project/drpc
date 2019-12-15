package com.distkv.drpc.api.worker;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorChooserFactory;

/**
 * @author zrj CreateDate: 2019/11/20
 */
public interface ExecutorChooser extends EventExecutorChooserFactory.EventExecutorChooser {

  EventExecutor next(int taskId);

}
