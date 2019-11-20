package org.dst.drpc;


import org.dst.drpc.api.Handler;
import org.dst.drpc.api.Server;
import org.dst.drpc.common.URL;
import org.dst.drpc.netty.NettyTransportFactory;
import org.dst.drpc.utils.NetUtils;


public class Exporter<T> {

  private T ref;
  private Class<T> interfaceClass;
  private URL serverUrl;

  public Exporter() {
    serverUrl = new URL();
    String localAddress = NetUtils.getLocalAddress().getHostAddress();
    serverUrl.setHost(localAddress);
  }

  public void setRef(T ref) {
    this.ref = ref;
  }

  public void isLocal(boolean isLocal) {
    if (isLocal) {
      serverUrl.setHost("127.0.0.1");
    }
  }

  public void setProtocol(String protocol) {
    serverUrl.setProtocol(protocol);
  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
    serverUrl.setPath(interfaceClass.getName());
  }

  public void setPort(int port) {
    serverUrl.setPort(port);
  }


  public void export() {
    Handler handler = new HandlerDelegate(new ServerImpl<>(ref, interfaceClass));
    Server server = NettyTransportFactory.getInstance().createServer(serverUrl, handler);
    server.open();
  }
}
