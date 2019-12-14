package com.distkv.drpc.benchmark.drpc;

import com.distkv.drpc.DrpcServer;
import com.distkv.drpc.benchmark.common.IService;
import com.distkv.drpc.benchmark.common.ISeviceImpl;
import com.distkv.drpc.config.ServerConfig;

public class DrpcProvider {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(25500)
        .build();

    DrpcServer drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(IService.class, new ISeviceImpl());
    drpcServer.run();
  }
}
