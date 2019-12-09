package com.distkv.drpc.async;

import com.distkv.drpc.DrpcServer;
import com.distkv.drpc.config.ServerConfig;

public class Server {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DrpcServer server = new DrpcServer(serverConfig);
    server.registerService(IServer.class, new IServerImpl());
    server.registerService(IServer2.class, new IServer2Impl());
    server.run();
  }

}
