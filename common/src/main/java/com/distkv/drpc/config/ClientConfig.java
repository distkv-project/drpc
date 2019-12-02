package com.distkv.drpc.config;

import com.distkv.drpc.exception.DrpcIllegalAddressException;
import lombok.Builder;

@Builder
public class ClientConfig {

  private String address;

  private int timeout;

  public int getServerPort() {
    if(address.contains("://")) {
      address = address.substring(address.indexOf("://") + "://".length());
    }
    String[] ip$port = address.split(":");
    if(ip$port.length != 2) {
      throw new DrpcIllegalAddressException("Multi ':' found in address, except one. Address: " + address);
    }
    try {
      return Integer.valueOf(ip$port[1]);
    } catch (NumberFormatException e) {
      throw new DrpcIllegalAddressException("Port is not Integer Type, Address: " + address, e);
    }
  }

  public String getServerIp() {
    if(address.contains("://")) {
      address = address.substring(address.indexOf("://") + "://".length());
    }
    String[] ip$port = address.split(":");
    if(ip$port.length != 2) {
      throw new DrpcIllegalAddressException("Multi ':' found in address, except one. Address: " + address);
    }
    return ip$port[0];
  }

  public int getTimeout() {
    return timeout;
  }
}
