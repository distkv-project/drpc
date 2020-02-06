package org.dousi.common;

import lombok.Getter;
import lombok.Setter;
import org.dousi.exception.DousiIllegalAddressException;

import java.util.Objects;

@Getter
@Setter
public class DousiAddress {

  private String ip;

  private int port;

  public DousiAddress() {

  }

  public DousiAddress(String address) {
    int index = address.indexOf(":");
    if (index <= 0) {
      throw new DousiIllegalAddressException(address);
    }
    String[] ipPort = address.split(":");
    this.ip = ipPort[0];
    this.port = Integer.valueOf(ipPort[1]);
  }

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

  public String getAddress() {
    return this.ip + ":" + this.port;
  }
}
