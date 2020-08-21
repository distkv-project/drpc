package org.drpc.exception;


public class DrpcTransportException extends RuntimeException {

  public DrpcTransportException() {
  }

  public DrpcTransportException(String message) {
    super(message);
  }

  public DrpcTransportException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcTransportException(Throwable cause) {
    super(cause);
  }

  public DrpcTransportException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
