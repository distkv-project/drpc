package com.distkv.drpc.config;

import com.distkv.drpc.common.DrpcAddress;
import com.distkv.drpc.utils.NetUtils;
import lombok.Builder;

@Builder
public class ServerConfig {

  private int port;

  private int workerThreadNum;

  public int getPort() {
    return port;
  }

  public int getWorkerThreadNum() {
    return workerThreadNum;
  }

  public DrpcAddress getDrpcAddress() {
    String serverIp = NetUtils.getLocalAddress().getHostAddress();
    return new DrpcAddress(serverIp, port);
  }

}
