package org.dst.drpc.api.async.facade;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.dst.drpc.api.async.Response;

/**
 * @author zrj CreateDate: 2019/11/1
 */
public class ResponseCompletableFutureFacade extends CompletableFuture implements Response {

  private long requestId;
  private Map<String, Object> attributes = new HashMap<>();

  @Override
  public long getRequestId() {
    return requestId;
  }

  @Override
  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  @Override
  public Object getValue() {
    try {
      return get();
    } catch (Exception e) {
      // should not happen
      return null;
    }
  }

  @Override
  public void setValue(Object value) {
    complete(value);
  }

  @Override
  public Throwable getThrowable() {
    try {
      return (Throwable) get();
    } catch (Exception e) {
      // should not happen
      return null;
    }
  }

  @Override
  public void setThrowable(Throwable throwable) {
    complete(throwable);
  }

  @Override
  public boolean hasAttribute(String key) {
    return attributes.containsKey(key);
  }

  @Override
  public Object getAttribute(String key) {
    return attributes.get(key);
  }

  @Override
  public void setAttribute(String key, Object value) {
    if (value == null) {
      attributes.remove(key);
    } else {
      attributes.put(key, value);
    }
  }

  @Override
  public void removeAttribute(String key) {
    attributes.remove(key);
  }
}
