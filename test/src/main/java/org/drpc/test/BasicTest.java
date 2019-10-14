package org.drpc.test;

import org.drpc.server.RpcServer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicTest {

  @Test
  public void testEcho() {
    RpcServer rpcServer = new RpcServer();
    rpcServer.registerService(null);
  }

}
