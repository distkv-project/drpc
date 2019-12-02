package com.distkv.drpc.pb;

import com.distkv.drpc.Exporter;

public class PBServer {

  public static void main(String[] args) {
    Exporter exporter = new Exporter();
    exporter.setProtocol("dst");
    exporter.registerService(IPBService.class, new PBServiceImpl());
    exporter.registerService(IPBService2.class, new PBServiceImpl2());
    exporter.isLocal(true);
    exporter.setPort(8080);
    exporter.export();
  }
}
