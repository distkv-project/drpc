package org.dst.drpc.api;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.dst.drpc.codec.Codec;
import org.dst.drpc.config.ServerConfig;
import org.dst.drpc.constants.GlobalConstants;
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
  private ExecutorService executor;

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
  public Executor getExecutor() {
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
    logger.info("Server opened, ip: " + serverConfig.getServerIp() + " port: " + serverConfig
        .getServerPort());
  }

  @Override
  public void close() {
    doClose();
    logger.info("Server closed, ip: " + serverConfig.getServerIp() + " port: " + serverConfig
        .getServerPort());
  }

  protected abstract void doOpen();

  protected abstract void doClose();

  private void createExecutor() {
    int workerNum = serverConfig.getWorkerThreadNum() > 0 ? serverConfig.getWorkerThreadNum()
        : GlobalConstants.threadNumber * 2;
    executor = Executors.newFixedThreadPool(workerNum);
  }
}
