package com.distkv.drpc.test.naming.zookeeper;

import common.DrpcServiceInstance;
import org.apache.curator.test.TestingServer;
import org.testng.Assert;
import org.testng.annotations.Test;
import registry.DrpcURL;
import registry.zookeeper.ZookeeperNamingService;

import java.io.IOException;
import java.util.List;

public class ZookeeperNamingServiceTest {
  private TestingServer zkServer;
  private DrpcURL url;
  private ZookeeperNamingService namingService;

  public void setup() throws Exception {
    zkServer = new TestingServer(2020, true);
    url = new DrpcURL("zookeeper://127.0.0.1:2020");
    namingService =  new ZookeeperNamingService(url);
  }

  public void tearDown() throws IOException {
    zkServer.stop();
  }

  @Test
  public void testPublishAndPull() throws Exception {
    setup();
//    System.out.println(zkServer.getConnectString());
    List<DrpcServiceInstance> instances =
          (List<DrpcServiceInstance>) namingService.pull(null);
    Assert.assertTrue(instances.isEmpty());

    namingService.publish(testService.class.getName(),"127.0.0.1:8012");
    instances = (List<DrpcServiceInstance>) namingService.pull(testService.class.getName());
    System.out.println(instances.size());
    for (DrpcServiceInstance instance : instances) {
      System.out.println(instance.toString());
    }
  }

}
