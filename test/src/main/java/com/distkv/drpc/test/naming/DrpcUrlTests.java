package com.distkv.drpc.test.naming;

import org.testng.Assert;
import org.testng.annotations.Test;
import registry.DrpcURL;

public class DrpcUrlTests {
  @Test
  public void testDistKvUrl() {
    String distkv = "distkv://127.0.0.1:8080";
    DrpcURL url = new DrpcURL(distkv);
    Assert.assertEquals(url.getProtocol(), "distkv");
    Assert.assertEquals(url.getHostsPorts(), "127.0.0.1:8080");
    Assert.assertEquals(url.getPath(), null);
    Assert.assertEquals(url.getQueryMap(), null);
  }

  @Test
  public void testOtherUrl() {
    String other = "file:///home/ubuntu/test.config?key1=value1&key2=value2";
    DrpcURL url = new DrpcURL(other);
    Assert.assertEquals(url.getProtocol(), "file");
    Assert.assertEquals(url.getHostsPorts(), "");
    Assert.assertEquals(url.getPath(), "home/ubuntu/test.config");
    Assert.assertEquals(url.getStringParameter("key1", "NULL"), "value1");
  }

}
