package org.dst.drpc.api.async;


public interface Response {

  long getRequestId();

  void setRequestId(long requestId);

  Object getValue();

  void setValue(Object value);

  Throwable getThrowable();

  void setThrowable(Throwable throwable);

  /**
   * has attribute.
   *
   * @param key key.
   * @return has or has not.
   */
  boolean hasAttribute(String key);

  /**
   * get attribute.
   *
   * @param key key.
   * @return value.
   */
  Object getAttribute(String key);

  /**
   * set attribute.
   *
   * @param key key.
   * @param value value.
   */
  void setAttribute(String key, Object value);

  /**
   * remove attribute.
   *
   * @param key key.
   */
  void removeAttribute(String key);

}
