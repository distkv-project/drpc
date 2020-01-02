package com.distkv.drpc.config;

import com.distkv.drpc.common.DrpcAddress;
import com.distkv.drpc.utils.NetUtils;
import lombok.Builder;

@Builder
public class ServerConfig {

  private int port;

  private int workerThreadNum;

  /**
   * Enable thread io only is true by default.
   *
   * If this is true,it means that `HashableExecutor` is needn't,a series
   * of requests can't be able to be executed by the origin order.
   * When you need keep order for a series of requests,you can set this to
   * be false, then the `HashableExecutor` will be null.
   *
   * If this is false the request will keep order,the requests will be
   * executed by a same thread.
   */
  @Builder.Default
  private boolean enableIOThreadOnly = true;

  /**
   * If every request from the same TCP should be executed by order, set this option true.
   *
   * You should be carefully to set this option true. If you do so, all requests from the same
   * rpc-client would be executed one by one in the same thread in rpc-server to guarantee to keep
   * the invoking order, when might cause bad performance.
   *
   * Actually, there are rarely conditions you should set this option true.
   */
  private boolean sequential = false;

  public int getPort() {
    return port;
  }

  public int getWorkerThreadNum() {
    return workerThreadNum;
  }

  public boolean isSequential() {
    return sequential;
  }

  public boolean enableIOThreadOnly() {
    return enableIOThreadOnly;
  }

  public DrpcAddress getDrpcAddress() {
    String serverIp = NetUtils.getLocalAddress().getHostAddress();
    return new DrpcAddress(serverIp, port);
  }

}
