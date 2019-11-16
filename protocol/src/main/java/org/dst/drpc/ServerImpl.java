package org.dst.drpc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dst.drpc.api.async.DefaultResponse;
import org.dst.drpc.api.async.Request;
import org.dst.drpc.api.async.Response;
import org.dst.drpc.common.Void;
import org.dst.drpc.exception.DrpcException;
import org.dst.drpc.utils.ReflectUtils;


public class ServerImpl<T> implements Invoker<T> {

  protected Map<String, Method> methodMap = new ConcurrentHashMap<>();

  private T ref;

  private Class<T> interfaceClazz;

  /**
   * cache all interface's methods
   */
  public ServerImpl(T ref, Class<T> interfaceClazz) {
    if (!interfaceClazz.isInterface()) {
      throw new DrpcException("ServerImpl: interfaceClazz is not a interface!");
    }
    this.ref = ref;
    this.interfaceClazz = interfaceClazz;
    List<Method> methods = ReflectUtils.parseMethod(interfaceClazz);
    for (Method method : methods) {
      String methodDesc = ReflectUtils.getMethodDesc(method);
      methodMap.putIfAbsent(methodDesc, method);
    }
  }

  @Override
  public Class<T> getInterface() {
    return interfaceClazz;
  }

  @Override
  public Response invoke(Request request) {
    Response response = new DefaultResponse();
    String methodName = ReflectUtils.getMethodDesc(request.getMethodName(), request.getArgsType());
    Method method = methodMap.get(methodName);
    if (method == null) {
      response.setThrowable(new DrpcException("ServerImpl: can't find method: " + methodName));
      return response;
    }
    try {
      Object value = method.invoke(ref, request.getArgsValue());
      if (value == null) {
        response.setValue(Void.getInstance());
      } else {
        response.setValue(value);
      }
    } catch (Exception e) {
      response.setThrowable(
          new DrpcException("ServerImpl: exception when invoke method: " + methodName, e));
    } catch (Error e) {
      response
          .setThrowable(new DrpcException("ServerImpl: error when invoke method: " + methodName, e));
    }
    return response;
  }

}
