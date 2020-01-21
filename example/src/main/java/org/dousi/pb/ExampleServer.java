package org.dousi.pb;

import org.dousi.DrpcServer;
import org.dousi.config.ServerConfig;

public class ExampleServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DrpcServer drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(ExampleService.class, new ExampleServiceImpl());
    drpcServer.run();
  }
}
