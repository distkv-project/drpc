package org.dst.drpc.pb;

import org.dst.drpc.Exporter;


public class PBServer {

  public static void main(String[] args) {
    Exporter exporter = new Exporter();
    exporter.setProtocol("dst");
    exporter.registerService(IPBServer.class, new PBServerImpl());
    exporter.registerService(IPBServer2.class, new PBServerImpl2());
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.export();
  }

}
