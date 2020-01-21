package org.dousi.exception;

public class DrpcIllegalAddressException extends RuntimeException {

  public DrpcIllegalAddressException() {
  }

  public DrpcIllegalAddressException(String message) {
    super(message);
  }

  public DrpcIllegalAddressException(String message, Throwable cause) {
    super(message, cause);
  }

  public DrpcIllegalAddressException(Throwable cause) {
    super(cause);
  }

  public DrpcIllegalAddressException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

