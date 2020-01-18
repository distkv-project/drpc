package com.distkv.drpc.test.naming;

import common.DrpcServiceInstance;
import exception.DrpcIllegalUrlException;
import org.testng.Assert;
import org.testng.annotations.Test;
import registry.DistKvNamingService;
import registry.DrpcURL;

import java.net.UnknownHostException;
import java.util.List;

public class DistKvNamingServiceTests {
  @Test
  public void testDistKv() throws UnknownHostException {
    String distKvUrl = "distkv://127.0.0.1:8020";
    DistKvNamingService distKvService = new DistKvNamingService(new DrpcURL(distKvUrl));
    Assert.assertEquals(distKvService.getHostPort(), "127.0.0.1:8020");
    List<DrpcServiceInstance> instances = distKvService.pullRegisteredService(null);
    System.out.println(instances.get(0).getIp() + ":" + instances.get(0).getPort());
    Assert.assertTrue(instances.contains(new DrpcServiceInstance("127.0.0.1",8020)));
    distKvService.unsubscribe(null);
  }

  @Test(expectedExceptions = DrpcIllegalUrlException.class)
  public void testException() throws UnknownHostException {
    DistKvNamingService testService = new DistKvNamingService(new DrpcURL(""));
    testService.pullRegisteredService(null);
  }
}
