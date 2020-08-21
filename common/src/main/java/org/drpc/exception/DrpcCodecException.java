package org.drpc.exception;


public class DrpcCodecException extends RuntimeException {

  public DrpcCodecException() {
  }

  public DrpcCodecException(String message) {
    super(message);
  }

  public DrpcCodecException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcCodecException(Throwable cause) {
    super(cause);
  }

  public DrpcCodecException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
