package common;

import com.distkv.drpc.common.DrpcAddress;

public class DrpcServiceInstance extends DrpcAddress {

  private String tag;

  public DrpcServiceInstance() {
    super();
  }

  public DrpcServiceInstance(String ip, int port) {
    super(ip, port);
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

}
