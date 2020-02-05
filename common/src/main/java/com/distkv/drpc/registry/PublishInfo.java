package registry;

import com.distkv.drpc.common.DrpcAddress;
import lombok.Getter;
import lombok.Setter;

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

  public PublishInfo(DrpcAddress address, String interfaceName) {
    super();
    this.host = address.getIp();
    this.port = address.getPort();
    this.interfaceName = interfaceName;
  }

  public String getAddress() {
    return host + ":" + port;
  }

}
