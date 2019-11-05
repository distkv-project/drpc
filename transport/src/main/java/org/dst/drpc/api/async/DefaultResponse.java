package org.dst.drpc.api.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zrj CreateDate: 2019/10/29
 */
public class DefaultResponse implements Response {

  public static final String IS_ASYNC = "IS_ASYNC";

  private long requestId;

  private Object value;

  private Throwable throwable;

  private Map<String, Object> attributes = new HashMap<>();

  public DefaultResponse() {
  }

  public DefaultResponse(long requestId) {
    this.requestId = requestId;
  }

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
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public Throwable getThrowable() {
    return throwable;
  }

  @Override
  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
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
