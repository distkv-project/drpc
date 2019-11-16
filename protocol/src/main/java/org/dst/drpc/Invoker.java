package org.dst.drpc;

import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
