package org.dousi.test.naming;

import org.dousi.registry.DousiURL;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DousiUrlTests {
  @Test
  public void testDistKvUrl() {
    String distkv = "distkv://127.0.0.1:8080";
    DousiURL url = new DousiURL(distkv);
    Assert.assertEquals(url.getProtocol(), "distkv");
    Assert.assertEquals(url.getHostsPorts(), "127.0.0.1:8080");
    Assert.assertEquals(url.getPath(), "/");
    Assert.assertTrue(url.getQueryMap().isEmpty());
  }

  @Test
  public void testOtherUrl() {
    String other = "file:///home/ubuntu/test.config?key1=value1&key2=value2";
    DousiURL url = new DousiURL(other);
    Assert.assertEquals(url.getProtocol(), "file");
    Assert.assertEquals(url.getHostsPorts(), "");
    Assert.assertEquals(url.getPath(), "/home/ubuntu/test.config");
    Assert.assertEquals(url.getStringParameter("key1", "NULL"), "value1");
  }

}
