package org.dousi.test.naming.zookeeper;

import org.apache.curator.test.TestingServer;
import org.dousi.common.DousiServiceInstance;
import org.dousi.registry.DousiURL;
import org.dousi.registry.zookeeper.ZookeeperNamingService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class ZookeeperNamingServiceTest {
  private TestingServer zkServer;
  private DousiURL url;
  private ZookeeperNamingService namingService;

  public void setup() throws Exception {
    zkServer = new TestingServer(2020, true);
    url = new DousiURL("zookeeper://127.0.0.1:2020");
    namingService =  new ZookeeperNamingService(url);
  }

  public void tearDown() throws IOException {
    zkServer.stop();
  }

  @Test
  public void testPublishUnPublishAndPull() throws Exception {
    setup();
    List<DousiServiceInstance> instances =
          (List<DousiServiceInstance>) namingService.pull(null);
    Assert.assertTrue(instances.isEmpty());

    namingService.publish(testService.class.getName(),"127.0.0.1:8012");
    instances = (List<DousiServiceInstance>) namingService.pull(testService.class.getName());
    Assert.assertEquals(instances.size(), 1);
    Assert.assertEquals(instances.get(0).getAddress(), "127.0.0.1:8012");
    namingService.unPublish(testService.class.getName(), "127.0.0.1:8012");
    instances = (List<DousiServiceInstance>) namingService.pull(testService.class.getName());
    Assert.assertEquals(instances.size(), 0);
    tearDown();
  }

  @Test
  public void testSubscribeUnSubscribe() {

  }

}
