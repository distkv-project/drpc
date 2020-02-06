package org.dousi.exception;


public class DousiRuntimeException extends RuntimeException {

  public DousiRuntimeException() {
  }

  public DousiRuntimeException(String message) {
    super(message);
  }

  public DousiRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiRuntimeException(Throwable cause) {
    super(cause);
  }

  public DousiRuntimeException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
