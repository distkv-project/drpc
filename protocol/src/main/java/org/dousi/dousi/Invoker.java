package org.dousi.dousi;

import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
