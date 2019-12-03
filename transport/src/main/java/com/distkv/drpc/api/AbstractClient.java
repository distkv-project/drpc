package com.distkv.drpc.api;

import com.distkv.drpc.config.ClientConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.distkv.drpc.api.async.Response;
import com.distkv.drpc.codec.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端，保存了当前已经发送的所有请求，并保存了返回的Future
 */
public abstract class AbstractClient implements Client {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final int NEW = 0;
  private static final int CONNECTED = 1;
  private static final int DISCONNECTED = 2;

  private ClientConfig clientConfig;
  private volatile int status = NEW;
  private Codec codec;

  private Map<Long, Response> currentTask = new ConcurrentHashMap<>();

  public AbstractClient(ClientConfig clientConfig, Codec codec) {
    this.clientConfig = clientConfig;
    this.codec = codec;
  }

  protected Response getResponseFuture(long requestId) {
    return currentTask.remove(requestId);
  }

  protected void addCurrentTask(long requestId, Response response) {
    currentTask.putIfAbsent(requestId, response);
  }

  @Override
  public Codec getCodec() {
    return codec;
  }

  @Override
  public ClientConfig getConfig() {
    return clientConfig;
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
