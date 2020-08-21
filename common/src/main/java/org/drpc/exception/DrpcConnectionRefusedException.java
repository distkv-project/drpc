package org.drpc.exception;


public class DrpcConnectionRefusedException extends DrpcException {

  public DrpcConnectionRefusedException() {
  }

  public DrpcConnectionRefusedException(String message) {
    super(message);
  }

  public DrpcConnectionRefusedException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcConnectionRefusedException(Throwable cause) {
    super(cause);
  }

  public DrpcConnectionRefusedException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
