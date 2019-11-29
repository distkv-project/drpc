package com.distkv.dst.drpc.api;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.distkv.dst.drpc.codec.Codec;
import com.distkv.dst.drpc.common.URL;
import com.distkv.dst.drpc.constants.GlobalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer implements Server {

  private static Logger logger = LoggerFactory.getLogger(AbstractServer.class);

  private static final int NEW = 0;
  private static final int CONNECTED = 1;
  private static final int DISCONNECTED = 2;

  private URL serverUrl;
  private volatile int status = NEW;
  private Codec codec;
  private RoutableHandler routableHandler;
  private ExecutorService executor;

  public AbstractServer(URL url,Codec codec) {
    serverUrl = url;
    this.codec = codec;
    routableHandler = new DefaultRoutableHandler();
    executor = Executors.newFixedThreadPool(GlobalConstants.threadNumber * 2);
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
  public URL getUrl() {
    return serverUrl;
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
  }

  protected abstract void doOpen();
  protected abstract void doClose();
}
