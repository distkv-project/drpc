package org.dst.drpc.pb;

import org.dst.drpc.Exporter;
import org.dst.drpc.async.IServer;
import org.dst.drpc.async.IServerImpl;
import org.dst.drpc.config.ServerConfig;


public class Server {

  public static void main(String[] args) {
    ServerConfig serverConfig = new ServerConfig();

    Exporter exporter = new Exporter(serverConfig);
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.export();
  }

}
