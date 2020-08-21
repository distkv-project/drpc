package org.drpc;

import org.drpc.api.Request;
import org.drpc.api.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
