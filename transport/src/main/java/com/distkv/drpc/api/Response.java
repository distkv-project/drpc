package com.distkv.drpc.api;

public interface Response {

  long getRequestId();

  void setRequestId(long requestId);

  Object getValue();

  void setValue(Object value);

  Throwable getThrowable();

  void setThrowable(Throwable throwable);

  Enum<?> getStatus();

  void setStatus(Enum<?> status);

  boolean isError();

  void build();

}
