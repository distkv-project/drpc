package org.dousi.exception;


public class DousiTransportException extends RuntimeException {

  public DousiTransportException() {
  }

  public DousiTransportException(String message) {
    super(message);
  }

  public DousiTransportException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiTransportException(Throwable cause) {
    super(cause);
  }

  public DousiTransportException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
