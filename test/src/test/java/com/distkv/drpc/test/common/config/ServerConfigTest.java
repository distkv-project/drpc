package com.distkv.drpc.test.common.config;

import com.distkv.drpc.config.ServerConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServerConfigTest {

  @Test
  public void testGetPort() {
    ServerConfig serverConfig = ServerConfig.builder()
        .workerThreadNum(5)
        .port(8082)
        .build();
    Assert.assertEquals(8082, serverConfig.getPort());
  }

  @Test
  public void testGetWorkerThreadNum() {
    ServerConfig serverConfig = ServerConfig.builder()
        .workerThreadNum(5)
        .port(8082)
        .build();
    Assert.assertEquals(5, serverConfig.getWorkerThreadNum());
  }

  @Test
  public void testGgetDrpcAddress() {
    ServerConfig serverConfig = ServerConfig.builder()
        .workerThreadNum(5)
        .port(8082)
        .build();
    Assert.assertEquals(8082, serverConfig.getDrpcAddress().getPort());
  }
}
