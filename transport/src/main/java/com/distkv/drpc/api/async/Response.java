package com.distkv.drpc.api.async;


public interface Response {

  long getRequestId();

  void setRequestId(long requestId);

  Object getValue();

  void setValue(Object value);

  Throwable getThrowable();

  void setThrowable(Throwable throwable);

  boolean hasAttribute(String key);

  Object getAttribute(String key);

  void setAttribute(String key, Object value);
  
  void removeAttribute(String key);

}
