package org.dousi.test.naming.zookeeper;

import org.apache.curator.test.TestingServer;
import org.dousi.common.DousiServiceInstance;
import org.dousi.registry.DousiURL;
import org.dousi.registry.NotifyListener;
import org.dousi.registry.zookeeper.ZookeeperNamingService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

    namingService.publish(TestService.class.getName(),"127.0.0.1:8012");
    instances = (List<DousiServiceInstance>) namingService.pull(TestService.class.getName());
    Assert.assertEquals(instances.size(), 1);
    Assert.assertEquals(instances.get(0).getAddress(), "127.0.0.1:8012");
    namingService.unPublish(TestService.class.getName(), "127.0.0.1:8012");
    instances = (List<DousiServiceInstance>) namingService.pull(TestService.class.getName());
    Assert.assertEquals(instances.size(), 0);
    tearDown();
  }

  @Test
  public void testSubscribeUnSubscribe() throws Exception {
    setup();
    final List<DousiServiceInstance> add = new ArrayList<>();
    final List<DousiServiceInstance> remove = new ArrayList<>();
    namingService.subscribe(TestService.class.getName(), new NotifyListener() {
      @Override
      public void notify(Collection<DousiServiceInstance> addList, Collection<DousiServiceInstance> removeList) {
        System.out.println("New subscribe time: " + System.currentTimeMillis());
        System.out.println("AddList size: " + addList.size());
        for (DousiServiceInstance instance : addList) {
          System.out.println(instance.toString());
        }
        add.addAll(addList);

        System.out.println("RemoveList size: " + removeList.size());
        for (DousiServiceInstance instance : removeList) {
          System.out.println(instance.toString());
        }
        remove.addAll(removeList);
      }
    });

    namingService.publish(TestService.class.getName(), "127.0.0.1:8012");
    System.out.println("Publish time: " + System.currentTimeMillis());
    Thread.sleep(1000);
    Assert.assertEquals(add.size(), 1);
    Assert.assertEquals(remove.size(), 0);
    Assert.assertEquals(add.get(0).getAddress(), "127.0.0.1:8012");
    add.clear();
    remove.clear();

    namingService.unPublish(TestService.class.getName(), "127.0.0.1:8012");
    System.out.println("UnPublish time: " + System.currentTimeMillis());
    Thread.sleep(1000);
    Assert.assertEquals(add.size(), 0);
    Assert.assertEquals(remove.size(), 1);
    Assert.assertEquals(remove.get(0).getAddress(), "127.0.0.1:8012");
    add.clear();
    remove.clear();

    namingService.unsubscribe(TestService.class.getName());

    namingService.publish(TestService.class.getName(), "127.0.0.1:8012");
    System.out.println("Publish time: " + System.currentTimeMillis());
    Thread.sleep(1000);
    Assert.assertEquals(add.size(), 0);
    Assert.assertEquals(remove.size(), 0);
    add.clear();
    remove.clear();

    namingService.unPublish(TestService.class.getName(), "127.0.0.1:8012");
    System.out.println("UnPublish time: " + System.currentTimeMillis());
    Thread.sleep(1000);
    Assert.assertEquals(add.size(), 0);
    Assert.assertEquals(remove.size(), 0);
    add.clear();
    remove.clear();

    tearDown();
  }
}
