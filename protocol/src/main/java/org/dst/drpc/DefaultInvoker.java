package org.dst.drpc;


import org.dst.drpc.api.Client;
import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.common.URL;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class DefaultInvoker<T> implements Invoker<T> {

  private Client client;

  public DefaultInvoker(Client client) {
    this.client = client;
  }

  @Override
  public URL getURL() {
    return null;
  }

  @Override
  public Class<T> getInterface() {
    return null;
  }

  @Override
  public Response invoke(Request request) {
    return client.invoke(request);
  }

}
