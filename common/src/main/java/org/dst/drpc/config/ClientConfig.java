package org.dst.drpc.config;


import org.dst.drpc.exception.IllegalAddressException;

public class ClientConfig {

  private boolean writable = true;

  private int serverPort;

  private String serverIp;

  private int timeout;

  public int getServerPort() {
    return serverPort;
  }

  public String getServerIp() {
    return serverIp;
  }

  public void setServerAddress(String address) {
    checkWritable();
    if(address.contains("://")) {
      address = address.substring(address.indexOf("://") + "://".length());
    }
    String[] ip$port = address.split(":");
    if(ip$port.length != 2) {
      throw new IllegalAddressException("Multi ':' found in address, except one. Address: " + address);
    }
    serverIp = ip$port[0];
    try {
      serverPort = Integer.valueOf(ip$port[1]);
    } catch (NumberFormatException e) {
      throw new IllegalAddressException("Port is not Integer Type, Address: " + address, e);
    }
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    checkWritable();
    this.timeout = timeout;
  }

  public void setReadOnly() {
    checkWritable();
    writable = false;
  }

  private void checkWritable() {
    if(!writable) {
      throw new IllegalStateException("ClientConfig is not writable");
    }
  }
}
