package org.drpc.test;

import org.drpc.DrpcServer;
import org.drpc.Proxy;
import org.drpc.api.Client;
import org.drpc.config.ClientConfig;
import org.drpc.config.ServerConfig;
import org.drpc.netty.DrpcClient;
import org.drpc.session.DrpcSession;
import org.drpc.test.common.IService;
import org.drpc.test.common.IServiceImpl;
import org.drpc.test.generated.EchoProtocol;
import java.util.concurrent.ExecutionException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LineOrderedTest {

  private DrpcServer drpcServer;

  private Client client;

  @BeforeTest
  public void beforeTest() {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .sequential(true)
        .build();

    drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(IService.class, new IServiceImpl());
    drpcServer.run();

    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    client = new DrpcClient(clientConfig);
    client.open();
  }

  @Test
  public void testOrder() {
    DrpcSession session = DrpcSession.createSession();
    Proxy<IService> proxy = new Proxy<>();
    proxy.setInterfaceClass(IService.class);
    IService service = proxy.getService(client, session);
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
