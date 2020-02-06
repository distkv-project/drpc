package org.dousi.registry;

import lombok.Getter;
import lombok.Setter;
import org.dousi.common.DousiAddress;

@Getter
@Setter
public class PublishInfo extends NamingOptions {

  private String host;

  private int port;

  private String interfaceName;

  public PublishInfo() {

  }

  public PublishInfo(NamingOptions op) {
    super(op);
  }

  public PublishInfo(NamingOptions op, String host, int port, String interfaceName) {
    super(op);
    this.host = host;
    this.port = port;
    this.interfaceName = interfaceName;
  }

  public PublishInfo(PublishInfo info) {
    super(info);
    this.host = info.getHost();
    this.port = info.getPort();
    this.interfaceName = info.getInterfaceName();
  }

  public PublishInfo(DousiAddress address, String interfaceName) {
    super();
    this.host = address.getIp();
    this.port = address.getPort();
    this.interfaceName = interfaceName;
  }

  public String getAddress() {
    return host + ":" + port;
  }

}
