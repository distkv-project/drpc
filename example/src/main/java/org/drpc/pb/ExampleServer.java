package org.drpc.pb;

import org.drpc.DrpcServer;
import org.drpc.config.ServerConfig;

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
