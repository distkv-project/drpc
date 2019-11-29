package com.distkv.dst.drpc;

import com.distkv.dst.drpc.api.async.Request;
import com.distkv.dst.drpc.api.async.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
