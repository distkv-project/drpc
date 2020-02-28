package org.dousi.test.transport.netty;

import org.dousi.api.Server;
import org.dousi.config.ServerConfig;
import org.dousi.netty.NettyServer;
import org.testng.annotations.Test;

import java.net.BindException;
import java.util.Collections;

public class NettyServerTest {

  @Test(expectedExceptions = BindException.class)
  public void testPortInUse() {
    ServerConfig config = ServerConfig.builder().port(8082).build();
    Server server1 = new NettyServer(config, Collections.emptyList());
    Server server2 = new NettyServer(config, Collections.emptyList());
    try {
      server1.open();
      server2.open();
    } finally {
      server1.close();
      server2.close();
    }
  }
}
