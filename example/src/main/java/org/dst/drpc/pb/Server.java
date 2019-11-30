package org.dst.drpc.pb;

import org.dst.drpc.Exporter;
import org.dst.drpc.async.IServer;
import org.dst.drpc.async.IServerImpl;


public class Server {

  public static void main(String[] args) {
    Exporter exporter = new Exporter();
    exporter.setProtocol("dst");
    exporter.registerService(IServer.class, new IServerImpl());
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.export();
  }

}
