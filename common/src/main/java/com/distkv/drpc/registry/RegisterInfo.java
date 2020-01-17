package registry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterInfo extends NamingOptions {

  private String host;

  private int port;

  private String interfaceName;

  public RegisterInfo() {

  }

  public RegisterInfo(NamingOptions op) {
    super(op);
  }

  public RegisterInfo(NamingOptions op, String host, int port, String interfaceName) {
    super(op);
    this.host = host;
    this.port = port;
    this.interfaceName = interfaceName;
  }

  public RegisterInfo(RegisterInfo info) {
    super(info);
    this.host = info.getHost();
    this.port = info.getPort();
    this.interfaceName = info.getInterfaceName();
  }

}
