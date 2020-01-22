package org.dousi.exception;

public class DousiIllegalAddressException extends RuntimeException {

  public DousiIllegalAddressException() {
  }

  public DousiIllegalAddressException(String message) {
    super(message);
  }

  public DousiIllegalAddressException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiIllegalAddressException(Throwable cause) {
    super(cause);
  }

  public DousiIllegalAddressException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

