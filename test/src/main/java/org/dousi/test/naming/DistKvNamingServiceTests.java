package org.dousi.test.naming;

import org.dousi.common.DousiServiceInstance;
import org.dousi.registry.DistKvNamingService;
import org.dousi.registry.DousiURL;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.List;

public class DistKvNamingServiceTests {
  @Test
  public void testDistKv() throws UnknownHostException {
    String distKvUrl = "distkv://127.0.0.1:8020";
    DistKvNamingService distKvService = new DistKvNamingService(new DousiURL(distKvUrl));
    Assert.assertEquals(distKvService.getHostPort(), "127.0.0.1:8020");
    List<DousiServiceInstance> instances = distKvService.pull(null);
    System.out.println(instances.get(0).getIp() + ":" + instances.get(0).getPort());
    Assert.assertTrue(instances.contains(new DousiServiceInstance("127.0.0.1",8020)));
    distKvService.unsubscribe(null);
  }

  @Test(expectedExceptions = DousiServiceInstance.class)
  public void testException() throws UnknownHostException {
    DistKvNamingService testService = new DistKvNamingService(new DousiURL(""));
    testService.pull(null);
  }
}
