package com.distkv.drpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import com.distkv.drpc.Invoker;
import com.distkv.drpc.api.AsyncResponse;
import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;
import com.distkv.drpc.exception.DrpcException;
import com.distkv.drpc.utils.RequestIdGenerator;


public class ProxyHandler<T> implements InvocationHandler {

  private Invoker invoker;

  private Class<T> interfaceClazz;

  public ProxyHandler(Class<T> clazz, Invoker invoker) {
    interfaceClazz = clazz;
    this.invoker = invoker;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (isLocalMethod(method)) {
      throw new DrpcException("Can not invoke local method: " + method.getName());
    }

    Request request = new Request();
    request.setRequestId(RequestIdGenerator.next());
    request.setInterfaceName(method.getDeclaringClass().getName());
    request.setMethodName(method.getName());
    request.setArgsValue(args);

    Class<?> returnType = method.getReturnType();
    Response response = invoker.invoke(request);

    // async-method
    if (CompletableFuture.class.isAssignableFrom(returnType)) {
      CompletableFuture future = new CompletableFuture();
      AsyncResponse asyncResponse = (AsyncResponse) response;
      asyncResponse.whenComplete((v, t) -> {
        if (t != null) {
          future.completeExceptionally(t);
        } else {
          if (v.getThrowable() != null) {
            future.completeExceptionally(v.getThrowable());
          } else {
            future.complete(v.getValue());
          }
        }
      });
      return future;
    }

    // sync-method
    if (response.getThrowable() != null) {
      throw response.getThrowable();
    }

    return response.getValue();
  }

  private boolean isLocalMethod(Method method) {
    if (method.getDeclaringClass().equals(Object.class)) {
      try {
        interfaceClazz
            .getDeclaredMethod(method.getName(), method.getParameterTypes());
        return false;
      } catch (NoSuchMethodException e) {
        return true;
      }
    }
    return false;
  }
}
