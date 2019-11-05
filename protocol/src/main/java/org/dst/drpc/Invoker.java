package org.dst.drpc;

import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.common.URL;


/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface Invoker<T> {

  URL getURL();

  Class<T> getInterface();

  Response invoke(Request request);

}
