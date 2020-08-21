package org.drpc.exception;


public class DrpcRuntimeException extends RuntimeException {

  public DrpcRuntimeException() {
  }

  public DrpcRuntimeException(String message) {
    super(message);
  }

  public DrpcRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcRuntimeException(Throwable cause) {
    super(cause);
  }

  public DrpcRuntimeException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
