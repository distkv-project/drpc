package com.distkv.drpc.api;

import com.distkv.drpc.api.worker.ExecutorChooser;
import com.distkv.drpc.api.worker.TaskHashedExecutor;
import com.distkv.drpc.api.worker.WorkerLoopGroup;
import com.distkv.drpc.codec.Codec;
import com.distkv.drpc.config.ServerConfig;
import com.distkv.drpc.constants.GlobalConstants;
import io.netty.util.concurrent.DefaultEventExecutorChooserFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorChooserFactory.EventExecutorChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer implements Server {

  private static Logger logger = LoggerFactory.getLogger(AbstractServer.class);

  private static final int NEW = 0;
  private static final int CONNECTED = 1;
  private static final int DISCONNECTED = 2;

  private ServerConfig serverConfig;
  private volatile int status = NEW;
  private Codec codec;
  private RoutableHandler routableHandler;
  private TaskHashedExecutor executor;

  public AbstractServer(ServerConfig serverConfig, Codec codec) {
    this.serverConfig = serverConfig;
    this.codec = codec;
    routableHandler = new DefaultRoutableHandler();
    createExecutor();
  }

  @Override
  public Codec getCodec() {
    return codec;
  }

  @Override
  public RoutableHandler getRoutableHandler() {
    return routableHandler;
  }

  @Override
  public TaskHashedExecutor getExecutor() {
    return executor;
  }

  @Override
  public ServerConfig getConfig() {
    return serverConfig;
  }

  @Override
  public boolean isOpen() {
    return status == CONNECTED;
  }

  @Override
  public void open() {
    doOpen();
  }

  @Override
  public void close() {
    doClose();
    executor.shutdownGracefully();
  }

  protected abstract void doOpen();

  protected abstract void doClose();

  private void createExecutor() {
    int workerNum = serverConfig.getWorkerThreadNum() > 0 ? serverConfig.getWorkerThreadNum()
        : GlobalConstants.THREAD_NUMBER * 2;
    executor = new WorkerLoopGroup(workerNum,
        (eventExecutors) ->
            new ExecutorChooser() {
              private final EventExecutor[] executors = eventExecutors;
              private final int size = eventExecutors.length;
              private EventExecutorChooser chooser = DefaultEventExecutorChooserFactory.INSTANCE
                  .newChooser(eventExecutors);

              @Override
              public EventExecutor next(int taskId) {
                return executors[taskId % size];
              }

              @Override
              public EventExecutor next() {
                return chooser.next();
              }

            });
  }

}
