package utils;

import com.distkv.drpc.exception.DrpcRuntimeException;

public class CheckNotNullUtill {
  public static boolean checkNotNullUtill(Object o) {
    if (o == null) {
      throw new DrpcRuntimeException("This object is NULL.");
    } else {
      return true;
    }
  }

  public static boolean checkStringEmptyOrNull(String s) {
    if (s == null) {
      throw new DrpcRuntimeException("This string is NULL.");
    }
    if (s.isEmpty()) {
      throw new DrpcRuntimeException("This string is EMPTY");
    }
    return true;
  }
}
