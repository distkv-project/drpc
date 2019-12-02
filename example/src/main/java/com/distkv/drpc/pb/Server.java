package com.distkv.drpc.pb;

import com.distkv.drpc.Exporter;
import com.distkv.drpc.config.ServerConfig;


public class Server {

  public static void main(String[] args) {
    ServerConfig serverConfig = new ServerConfig();
    serverConfig.setServerPort(8080);

    Exporter exporter = new Exporter(serverConfig);
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.export();
  }

}
