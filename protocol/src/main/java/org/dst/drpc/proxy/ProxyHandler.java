package org.dst.drpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import org.dst.drpc.Invoker;
import org.dst.drpc.api.async.AsyncResponse;
import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.common.URL;
import org.dst.drpc.common.Void;
import org.dst.drpc.exception.DstException;
import org.dst.drpc.utils.RequestIdGenerator;


public class ProxyHandler<T> implements InvocationHandler {

  private URL url;

  private Invoker invoker;

  private Class<T> interfaceClazz;

  public ProxyHandler(Class<T> clazz, URL url, Invoker invoker) {
    interfaceClazz = clazz;
    this.url = url;
    this.invoker = invoker;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (isLocalMethod(method)) {
      if ("toString".equals(method.getName())) {
        return "";
      }
      if ("equals".equals(method.getName())) {
        return false;
      }
      throw new DstException("can not invoke local method:" + method.getName());
    }


    Request request = new Request();
    request.setRequestId(RequestIdGenerator.next());
    request.setInterfaceName(method.getDeclaringClass().getName());
    request.setMethodName(method.getName());
    request.setArgsType(getArgsTypeString(method));
    request.setArgsValue(args);

    Class<?> returnType = method.getReturnType();
    if(returnType == java.lang.Void.TYPE) {
      request.setReturnType(Void.class);
    } else {
      request.setReturnType(returnType);
    }

    Response response = invoker.invoke(request);

    if(CompletableFuture.class.isAssignableFrom(returnType)) {
      CompletableFuture future = new CompletableFuture();
      AsyncResponse asyncResponse = (AsyncResponse) response;
      asyncResponse.whenComplete((v,t) -> {
        if(t != null) {
          future.completeExceptionally(t);
        } else {
          if(v.getThrowable() != null) {
            future.completeExceptionally(v.getThrowable());
          } else {
            future.complete(v.getValue());
          }
        }
      });
      return future;
    }

    // 如果不是异步的，直接同步返回结果。
    if (response.getThrowable() != null) {
      throw response.getThrowable();
    }

    return response.getValue();
  }

  private String getArgsTypeString(Method method) {
    Class<?>[] pts = method.getParameterTypes();
    if (pts.length <= 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Class clazz : pts) {
      sb.append(clazz.getName()).append(",");
    }
    if (sb.length() > 0) {
      sb.setLength(sb.length() - ",".length());
    }
    return sb.toString();
  }

  /**
   * tostring,equals,hashCode,finalize等接口未声明的方法不进行远程调用
   */
  public boolean isLocalMethod(Method method) {
    if (method.getDeclaringClass().equals(Object.class)) {
      try {
        Method interfaceMethod = interfaceClazz
            .getDeclaredMethod(method.getName(), method.getParameterTypes());
        return false;
      } catch (Exception e) {
        return true;
      }
    }
    return false;
  }
}
