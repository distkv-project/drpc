package com.distkv.drpc.test.common.utils;

import com.distkv.drpc.utils.RequestIdGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestIdGeneratorTest {

  @Test
  public void testNext() {
    Assert.assertNotEquals(RequestIdGenerator.next(), 0);
    Assert.assertEquals(RequestIdGenerator.next(), 2);
    Assert.assertEquals(RequestIdGenerator.next(), 3);
    Assert.assertEquals(RequestIdGenerator.next(), 4);
    Assert.assertNotEquals(RequestIdGenerator.next(), 6);
  }

}
