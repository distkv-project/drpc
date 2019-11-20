package org.dst.drpc.utils;

import java.util.concurrent.atomic.LongAdder;


public class RequestIdGenerator {

  private final static LongAdder curId = new LongAdder();
  private final static long MAX_PER_ROUND = 1 << 24;

  public static long next() {
    if (curId.longValue() >= MAX_PER_ROUND) {
      synchronized (RequestIdGenerator.class) {
        if (curId.longValue() >= MAX_PER_ROUND) {
          curId.reset();
        }
      }
    }
    curId.increment();
    return curId.longValue();
  }
}
