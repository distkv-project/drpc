package org.dst.drpc;


import org.dst.drpc.api.Handler;
import org.dst.drpc.api.async.Request;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class HandlerDelegate implements Handler {

  private Invoker serverImpl;

  public HandlerDelegate(Invoker serverImpl) {
    this.serverImpl = serverImpl;
  }

  @Override
  public String getServerName() {
    return serverImpl.getInterface().getName();
  }

  @Override
  public Object handle(Object message) {
    return serverImpl.invoke((Request) message);
  }
}
