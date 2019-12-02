package com.distkv.drpc.pb;

import com.distkv.drpc.Exporter;
import com.distkv.drpc.config.ServerConfig;


public class Server {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .serverPort(8080)
        .build();

    Exporter exporter = new Exporter(serverConfig);
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.export();
  }

}
