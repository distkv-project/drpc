package com.distkv.drpc.common;

import com.distkv.drpc.exception.DrpcIllegalAddressException;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class DrpcAddress {

  private String ip;

  private int port;

  public DrpcAddress() {

  }

  public DrpcAddress(String address) {
    int index = address.indexOf(":");
    if (index <= 0) {
      throw new DrpcIllegalAddressException(address);
    }
    String[] ipPort = address.split(":");
    this.ip = ipPort[0];
    this.port = Integer.valueOf(ipPort[1]);
  }

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
