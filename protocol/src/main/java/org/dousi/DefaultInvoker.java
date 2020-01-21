package org.dousi;

import org.dousi.api.Client;
import org.dousi.api.Request;
import org.dousi.api.Response;


public class DefaultInvoker<T> implements Invoker<T> {

  private Client client;
  private Class<T> interfaceClass;

  public DefaultInvoker(Client client, Class<T> interfaceClass) {
    this.client = client;
    this.interfaceClass = interfaceClass;
  }

  @Override
  public Class<T> getInterface() {
    return interfaceClass;
  }

  @Override
  public Response invoke(Request request) {
    return client.invoke(request);
  }

}
