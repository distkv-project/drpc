package org.dousi.common;

import java.util.Objects;


public class DousiAddress {

  private String ip;
  private int port;

  public DousiAddress(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DousiAddress)) {
      return false;
    }
    DousiAddress other = (DousiAddress) obj;
    if (!Objects.equals(ip, other.ip)) {
      return false;
    }
    return Objects.equals(port, other.port);
  }
}
