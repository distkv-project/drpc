package registry;

public class RegisterInfo extends NamingOptions {

  private String host;

  private String port;

  private String interfaceName;

  public RegisterInfo() {

  }

  public RegisterInfo(NamingOptions op) {
    super(op);
  }

  public RegisterInfo(NamingOptions op, String host, String port, String interfaceName) {
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

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }
}
