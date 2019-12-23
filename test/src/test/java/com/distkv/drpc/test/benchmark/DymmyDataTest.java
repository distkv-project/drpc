package com.distkv.drpc.test.benchmark;

import com.distkv.drpc.test.common.DymmyData;
import java.util.HashSet;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DymmyDataTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testOverSize() {
    new DymmyData(1024 * 1024 + 1);
  }

  @Test
  public void testNoRepeated() {
    Set<DymmyData> dataSet = new HashSet<>();
    for (int j = 0; j < 100000; j++) {
      DymmyData newData = new DymmyData(10);
      Assert.assertTrue(!dataSet.contains(newData));
      dataSet.add(newData);
    }
  }
}
