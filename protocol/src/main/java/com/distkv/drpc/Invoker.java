package com.distkv.drpc;

import com.distkv.drpc.api.async.Request;
import com.distkv.drpc.api.async.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
