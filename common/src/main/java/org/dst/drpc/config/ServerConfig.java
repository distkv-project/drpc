package org.dst.drpc.config;


import org.dst.drpc.model.DrpcAddress;
import org.dst.drpc.utils.NetUtils;

public class ServerConfig {

  private boolean writable = true;

  private int serverPort;

  private String serverIp;

  private int workerThreadNum;


  public String getServerIp() {
    return serverIp;
  }

  public void setServerIp(String serverIp) {
    checkWritable();
    this.serverIp = serverIp;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    checkWritable();
    this.serverPort = serverPort;
  }

  public int getWorkerThreadNum() {
    return workerThreadNum;
  }

  public void setWorkerThreadNum(int workerThreadNum) {
    checkWritable();
    this.workerThreadNum = workerThreadNum;
  }

  public DrpcAddress getDrpcAddress() {
    if(serverIp == null || "".equals(serverIp)) {
      serverIp = NetUtils.getLocalAddress().getHostAddress();
    }
    return new DrpcAddress(serverIp, serverPort);
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
