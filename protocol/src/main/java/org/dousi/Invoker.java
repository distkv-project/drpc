package org.dousi;

import org.dousi.api.Request;
import org.dousi.api.Response;


public interface Invoker<T> {

  Class<T> getInterface();

  Response invoke(Request request);

}
