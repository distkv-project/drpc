package org.drpc.config;

import org.drpc.exception.DrpcIllegalAddressException;
import lombok.Builder;

@Builder
public class ClientConfig {

  private String address;

  private int timeout;

  public int getServerPort() {
    if (address.contains("://")) {
      address = address.substring(address.indexOf("://") + "://".length());
    }
    String[] ipPort = address.split(":");
    if (ipPort.length != 2) {
      throw new DrpcIllegalAddressException(
          "Multi ':' found in address, except one. Address: " + address);
    }
    try {
      return Integer.valueOf(ipPort[1]);
    } catch (NumberFormatException e) {
      throw new DrpcIllegalAddressException("Port is not Integer Type, Address: " + address, e);
    }
  }

  public String getServerIp() {
    if (address.contains("://")) {
      address = address.substring(address.indexOf("://") + "://".length());
    }
    String[] ipPort = address.split(":");
    if (ipPort.length != 2) {
      throw new DrpcIllegalAddressException(
          "Multi ':' found in address, except one. Address: " + address);
    }
    return ipPort[0];
  }

  public int getTimeout() {
    return timeout;
  }
}
