package org.dousi.test;

import org.dousi.DousiServer;
import org.dousi.DousiSession;
import org.dousi.Proxy;
import org.dousi.api.Client;
import org.dousi.config.ClientConfig;
import org.dousi.config.ServerConfig;
import org.dousi.netty.NettyClient;
import org.dousi.test.common.IService;
import org.dousi.test.common.IServiceImpl;
import org.dousi.test.generated.EchoProtocol;
import java.util.concurrent.ExecutionException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LineOrderedTest {

  private DousiServer dousiServer;

  private Client client;

  @BeforeTest
  public void beforeTest() {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .sequential(true)
        .build();

    dousiServer = new DousiServer(serverConfig);
    dousiServer.registerService(IService.class, new IServiceImpl());
    dousiServer.run();

    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    client = new NettyClient(clientConfig);
    client.open();
  }

  @Test
  public void testOrder() {
    Proxy<IService> proxy = new Proxy<>();
    proxy.setInterfaceClass(IService.class);
    DousiSession session = DousiSession.fromRandom();
    IService service = proxy.getService(client,session);
    for (int i = 0; i < 10000; i++) {
      EchoProtocol.PutRequest putRequest = EchoProtocol.PutRequest
          .newBuilder()
          .setValue(i)
          .build();

      service.put(putRequest);

      try {
        EchoProtocol.GetResponse response = service.get().get();
        Assert.assertEquals(response.getValue(), i);
      } catch (InterruptedException | ExecutionException e) {
        Assert.fail("Error occurred!");
      }
    }
  }
}
