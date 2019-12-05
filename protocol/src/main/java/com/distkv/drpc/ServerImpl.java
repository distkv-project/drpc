package com.distkv.drpc;

import com.distkv.drpc.api.DefaultResponse;
import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;
import com.distkv.drpc.codec.DelayDeserialization;
import com.distkv.drpc.codec.Serialization;
import com.distkv.drpc.common.Void;
import com.distkv.drpc.exception.DrpcException;
import com.distkv.drpc.utils.ReflectUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
      String methodName = method.getName();
      methodMap.putIfAbsent(methodDesc, method);
      if (methodMap.containsKey(methodName)) {
        throw new DrpcException(
            "Duplicated method name: " + method.getDeclaringClass() + "#" + method
                + ". Method name excepted unique.");
      }
      methodMap.put(method.getName(), method);
    }
  }

  @Override
  public Class<T> getInterface() {
    return interfaceClazz;
  }

  @Override
  public Response invoke(Request request) {
    Response response = new DefaultResponse();
    String methodName = request.getMethodName();
    Method method = methodMap.get(methodName);
    if (method == null) {
      response.setThrowable(new DrpcException("ServerImpl: can't find method: " + methodName));
      return response;
    }
    try {
      Object[] argsValue = resolveArgsValue(request.getArgsValue(), method);
      Object value = method.invoke(ref, argsValue);
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
          .setThrowable(
              new DrpcException("ServerImpl: error when invoke method: " + methodName, e));
    }
    return response;
  }

  private Object[] resolveArgsValue(Object[] args, Method method) throws IOException {
    if (args == null) {
      return null;
    }
    Object[] resolvedArgs = new Object[args.length];
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof DelayDeserialization) {
        byte[] argData = ((DelayDeserialization) args[i]).getData();
        Serialization serialization = ((DelayDeserialization) args[i]).getSerialization();
        resolvedArgs[i] = serialization.deserialize(argData, parameterTypes[i]);
      } else {
        resolvedArgs[i] = args[i];
      }
    }
    return resolvedArgs;
  }

}
