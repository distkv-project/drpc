package com.distkv.drpc.utils;

import java.util.concurrent.atomic.LongAdder;


public class RequestIdGenerator {

  private static final LongAdder curId = new LongAdder();
  private static final long MAX_PER_ROUND = 1 << 24;

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
