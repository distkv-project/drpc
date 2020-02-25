package org.dousi.exception;


public class DousiConnectionRefusedException extends DousiException {

  public DousiConnectionRefusedException() {
  }

  public DousiConnectionRefusedException(String message) {
    super(message);
  }

  public DousiConnectionRefusedException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiConnectionRefusedException(Throwable cause) {
    super(cause);
  }

  public DousiConnectionRefusedException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
