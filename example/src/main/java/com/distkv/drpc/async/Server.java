package com.distkv.drpc.async;

import com.distkv.drpc.Exporter;


public class Server {

  public static void main(String[] args) {
    Exporter exporter = new Exporter();
    exporter.setProtocol("dst");
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.registerService(IServer2.class, new IServer2Impl());
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.export();
  }

}
