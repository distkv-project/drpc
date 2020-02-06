package org.dousi.exception;


public class DousiCodecException extends RuntimeException {

  public DousiCodecException() {
  }

  public DousiCodecException(String message) {
    super(message);
  }

  public DousiCodecException(String message, Throwable cause) {
    super(message, cause);
  }

  public DousiCodecException(Throwable cause) {
    super(cause);
  }

  public DousiCodecException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
