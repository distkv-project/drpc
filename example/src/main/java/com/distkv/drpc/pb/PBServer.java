package com.distkv.drpc.pb;

import com.distkv.drpc.Exporter;
import com.distkv.drpc.config.ServerConfig;

public class PBServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    Exporter exporter = new Exporter(serverConfig);
    exporter.registerService(IPBService.class, new PBServiceImpl());
    exporter.registerService(IPBService2.class, new PBServiceImpl2());
    exporter.export();
  }
}
