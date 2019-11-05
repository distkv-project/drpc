package org.dst.drpc.async;

import org.dst.drpc.Exporter;

/**
 * @author zrj CreateDate: 2019/10/28
 */
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
