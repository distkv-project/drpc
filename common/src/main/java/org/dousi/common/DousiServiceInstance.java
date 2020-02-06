package org.dousi.common;

public class DousiServiceInstance extends DousiAddress {

  private String tag;

  public DousiServiceInstance() {
    super();
  }

  public DousiServiceInstance(DousiAddress address) {
    super();
    this.setIp(address.getIp());
    this.setPort(address.getPort());
  }

  public DousiServiceInstance(String ip, int port) {
    super(ip, port);
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return this.tag + "/" + this.getIp() + ":" + this.getPort();
  }

}
