package com.distkv.drpc.async;

import com.distkv.drpc.Exporter;
import com.distkv.drpc.config.ServerConfig;

public class Server {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    Exporter exporter = new Exporter(serverConfig);
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.registerService(IServer2.class, new IServer2Impl());
    exporter.export();
  }

}
