package com.distkv.drpc.api.worker;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorChooserFactory;
import io.netty.util.concurrent.EventExecutorChooserFactory.EventExecutorChooser;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 业务线程组，借用netty的EventExecutorGroup实现，为了使用自定义EventExecutorChooserFactory
 *
 * @author zrj CreateDate: 2019/11/20
 */
final public class WorkerLoopGroup extends MultithreadEventExecutorGroup implements TaskHashedExecutor {

  private static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Integer.MAX_VALUE;

  private ExecutorChooser chooser;
  private static List<EventExecutor> children = new ArrayList<>(); // 抢在<init>方法执行之前


  public WorkerLoopGroup(int nThreads, EventExecutorChooserFactory chooserFactory) {
    this(nThreads, null, chooserFactory, DEFAULT_MAX_PENDING_EXECUTOR_TASKS,
        RejectedExecutionHandlers.reject());
    EventExecutorChooser chooser = chooserFactory.newChooser(children.toArray(new EventExecutor[0]));
    if(chooser instanceof ExecutorChooser) {
      this.chooser = (ExecutorChooser) chooser;
    } else {
      throw new IllegalArgumentException("WorkerLoopGroup必须使用com.nowcoder.pk.core.ExecutorChooser的chooser");
    }
  }

  private WorkerLoopGroup(int nThreads, Executor executor,
      EventExecutorChooserFactory chooserFactory, Object... args) {
    super(nThreads, executor, chooserFactory, args);
  }

  // TaskHashedExecutor
  @Override
  public void submit(int taskId, Runnable task) {
    chooser.next(taskId).submit(task);
  }


  // EventExecutorGroup
  @Override
  protected EventExecutor newChild(Executor executor, Object... args) throws Exception {
    EventExecutor newChildren = new DefaultEventExecutor(this, executor, (Integer) args[0], (RejectedExecutionHandler) args[1]);
    children.add(newChildren);
    return newChildren;
  }

  /**
   * 所有netty提供的提交任务的方法都不再建议使用。
   */
  @Override
  @Deprecated
  public Future<?> submit(Runnable task) {
    return next().submit(task);
  }

  @Override
  @Deprecated
  public <T> Future<T> submit(Runnable task, T result) {
    return next().submit(task, result);
  }

  @Override
  @Deprecated
  public <T> Future<T> submit(Callable<T> task) {
    return next().submit(task);
  }

  @Override
  @Deprecated
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    return next().schedule(command, delay, unit);
  }

  @Override
  @Deprecated
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    return next().schedule(callable, delay, unit);
  }

  @Override
  @Deprecated
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
    return next().scheduleAtFixedRate(command, initialDelay, period, unit);
  }

  @Override
  @Deprecated
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    return next().scheduleWithFixedDelay(command, initialDelay, delay, unit);
  }

  @Override
  @Deprecated
  public <T> List<java.util.concurrent.Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return next().invokeAll(tasks);
  }

  @Override
  @Deprecated
  public <T> List<java.util.concurrent.Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
    return next().invokeAll(tasks, timeout, unit);
  }

  @Override
  @Deprecated
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return next().invokeAny(tasks);
  }

  @Override
  @Deprecated
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return next().invokeAny(tasks, timeout, unit);
  }

  @Override
  @Deprecated
  public void execute(Runnable command) {
    next().execute(command);
  }
}
