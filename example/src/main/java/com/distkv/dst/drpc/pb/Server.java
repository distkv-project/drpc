package com.distkv.dst.drpc.pb;

import com.distkv.dst.drpc.Exporter;


public class Server {

  public static void main(String[] args) {
    IServer impl = new IServerImpl();
    Exporter<IServer> exporter = new Exporter<>();
    exporter.setProtocol("dst");
    exporter.setInterfaceClass(IServer.class);
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.setRef(impl);
    exporter.export();
  }

}
