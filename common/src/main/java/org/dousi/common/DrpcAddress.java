package org.dousi.common;

import java.util.Objects;


public class DrpcAddress {

  private String ip;
  private int port;

  public DrpcAddress(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DrpcAddress)) {
      return false;
    }
    DrpcAddress other = (DrpcAddress) obj;
    if (!Objects.equals(ip, other.ip)) {
      return false;
    }
    return Objects.equals(port, other.port);
  }
}
