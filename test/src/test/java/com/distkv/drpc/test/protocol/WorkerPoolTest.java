package com.distkv.drpc.test.protocol;


import com.distkv.drpc.DrpcServer;
import com.distkv.drpc.Proxy;
import com.distkv.drpc.api.Client;
import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.config.ServerConfig;
import com.distkv.drpc.netty.NettyClient;
import com.distkv.drpc.test.protocol.common.IService;
import com.distkv.drpc.test.protocol.common.IServiceImpl;
import com.distkv.drpc.test.protocol.generated.EchoProtocol;
import java.util.concurrent.ExecutionException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WorkerPoolTest {

  private DrpcServer drpcServer;

  private Client client;

  @BeforeTest
  public void beforeTest() {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(IService.class, new IServiceImpl());
    drpcServer.run();

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
    IService service = proxy.getService(client);
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
