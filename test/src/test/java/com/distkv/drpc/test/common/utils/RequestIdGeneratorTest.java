package com.distkv.drpc.test.common.utils;

import com.distkv.drpc.utils.RequestIdGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestIdGeneratorTest {

  @Test
  public void testNext() {
    long begin = RequestIdGenerator.next();
    RequestIdGenerator.next();
    RequestIdGenerator.next();
    long end = RequestIdGenerator.next();
    Assert.assertEquals(end - begin, 3);
  }

  @Test
  public void testIDOverFlow() {
    int maxPerRound = 1 << 24;
    for (int i = 0; i < maxPerRound; i++) {
      RequestIdGenerator.next();
    }
    RequestIdGenerator.next();
    Assert.assertEquals(RequestIdGenerator.next() < maxPerRound, true);

  }
}
