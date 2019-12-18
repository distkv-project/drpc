package com.distkv.drpc.pb;

import com.distkv.drpc.DrpcServer;
import com.distkv.drpc.config.ServerConfig;

public class PBServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DrpcServer drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(IPBService.class, new PBServiceImpl());
    drpcServer.registerService(IPBService2.class, new PBServiceImpl2());
    drpcServer.run();
  }
}
