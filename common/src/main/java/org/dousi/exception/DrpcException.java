package org.dousi.exception;


public class DrpcException extends RuntimeException {

  public DrpcException() {
  }

  public DrpcException(String message) {
    super(message);
  }

  public DrpcException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcException(Throwable cause) {
    super(cause);
  }

  public DrpcException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
