package org.dousi.exception;


public class DousiException extends RuntimeException {

  public DousiException() {
  }

  public DousiException(String message) {
    super(message);
  }

  public DousiException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiException(Throwable cause) {
    super(cause);
  }

  public DousiException(String message, Throwable cause, boolean enableSuppression,
                        boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
