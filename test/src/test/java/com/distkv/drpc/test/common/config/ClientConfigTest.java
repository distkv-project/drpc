package com.distkv.drpc.test.common.config;

import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.exception.DrpcIllegalAddressException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClientConfigTest {

  @Test
  public void testGetServerPort() {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("list://127.0.0.1:8080")
        .timeout(1000)
        .build();
    Assert.assertEquals(8080, clientConfig.getServerPort());
  }

  @Test(expectedExceptions = DrpcIllegalAddressException.class)
  public void testGetServerPortDrpcIllegalAddressException() {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("list://127.0.0.1:8080:8081")
        .timeout(1000)
        .build();
    clientConfig.getServerPort();

  }

  @Test(expectedExceptions = DrpcIllegalAddressException.class)
  public void testGetServerPortNumberFormatException() {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("list://127.0.0.1:abcd")
        .timeout(1000)
        .build();
    clientConfig.getServerPort();
  }

  @Test
  public void testGetServerIp() {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("list://127.0.0.1:8080")
        .timeout(1000)
        .build();
    Assert.assertEquals("127.0.0.1", clientConfig.getServerIp());
  }

  @Test(expectedExceptions = DrpcIllegalAddressException.class)
  public void testGetServerIpDrpcIllegalAddressException() {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("list://127.0.0.1:8080:8081")
        .timeout(1000)
        .build();
    clientConfig.getServerIp();
  }
}
