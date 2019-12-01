package org.dst.drpc.exception;

/**
 * @author zrj CreateDate: 2019/12/1
 */
public class IllegalAddressException extends RuntimeException {

  public IllegalAddressException() {
  }

  public IllegalAddressException(String message) {
    super(message);
  }

  public IllegalAddressException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalAddressException(Throwable cause) {
    super(cause);
  }

  public IllegalAddressException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
