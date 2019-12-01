package com.distkv.drpc.pb;

import com.distkv.drpc.Exporter;
import com.distkv.drpc.async.IServer;
import com.distkv.drpc.async.IServerImpl;
import com.distkv.drpc.Exporter;
import com.distkv.drpc.async.IServer;
import com.distkv.drpc.async.IServerImpl;


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
