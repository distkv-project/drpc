package org.drpc.test.transport;

import org.drpc.DrpcServer;
import org.drpc.config.ServerConfig;
import org.testng.annotations.Test;

public class DrpcServerTest {

  @Test
  public void  testDrpcServer() {
    ServerConfig config = ServerConfig.builder()
        .port(8082)
        .enableIOThreadOnly(true)
        .build();
    DrpcServer server = new DrpcServer(config);
    server.run();
    server.stop();
  }
}
