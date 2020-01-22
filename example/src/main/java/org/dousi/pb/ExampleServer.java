package org.dousi.pb;

import org.dousi.DousiServer;
import org.dousi.config.ServerConfig;

public class ExampleServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DousiServer drpcServer = new DousiServer(serverConfig);
    drpcServer.registerService(ExampleService.class, new ExampleServiceImpl());
    drpcServer.run();
  }
}
