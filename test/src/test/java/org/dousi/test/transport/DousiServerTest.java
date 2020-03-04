package org.dousi.test.transport;

import org.dousi.DousiServer;
import org.dousi.config.ServerConfig;
import org.testng.annotations.Test;

public class DousiServerTest {

  @Test
  public void  testDousiServer() {
    ServerConfig config = ServerConfig.builder()
        .port(8082)
        .enableIOThreadOnly(true)
        .build();
    DousiServer server = new DousiServer(config);
    server.run();
    server.stop();
  }
}
