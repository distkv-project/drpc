package org.dst.drpc.pb;

import org.dst.drpc.Exporter;


public class PBServer {

  public static void main(String[] args) {
    IPBServer impl = new PBServerImpl();
    Exporter<IPBServer> exporter = new Exporter<>();
    exporter.setProtocol("dst");
    exporter.setInterfaceClass(IPBServer.class);
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.setRef(impl);
    exporter.export();
  }

}
