package org.dousi.utils;

import org.dousi.exception.DousiRuntimeException;

public class CheckNotNullUtill {
  public static boolean checkNotNullUtill(Object o) {
    if (o == null) {
      throw new DousiRuntimeException("This object is NULL.");
    } else {
      return true;
    }
  }

  public static boolean checkStringEmptyOrNull(String s) {
    if (s == null) {
      throw new DousiRuntimeException("This string is NULL.");
    }
    if (s.isEmpty()) {
      throw new DousiRuntimeException("This string is EMPTY");
    }
    return true;
  }
}
