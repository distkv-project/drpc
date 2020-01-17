package registry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeInfo extends NamingOptions {

  private String interfaceName;

  public SubscribeInfo() {

  }

  public SubscribeInfo(NamingOptions op, String interfaceName) {
    super(op);
    this.interfaceName = interfaceName;
  }

  public SubscribeInfo(SubscribeInfo info) {
    super(info);
    this.interfaceName = info.getInterfaceName();
  }

}
