package org.dousi;

import org.dousi.api.DefaultResponse;
import org.dousi.api.Request;
import org.dousi.api.Response;
import org.dousi.codec.generated.DrpcProtocol.DrpcStatus;
import org.dousi.common.Void;
import org.dousi.exception.DrpcException;
import org.dousi.utils.ReflectUtils;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
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
      response.setStatus(DrpcStatus.OUTER_ERROR);
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
      response.setStatus(DrpcStatus.OK);
    } catch (Exception e) {
      response.setStatus(DrpcStatus.INNER_ERROR);
      response.setThrowable(
          new DrpcException("ServerImpl: exception when invoke method: " + methodName, e));
    } catch (Error e) {
      response.setStatus(DrpcStatus.INNER_ERROR);
      response
          .setThrowable(
              new DrpcException("ServerImpl: error when invoke method: " + methodName, e));
    }
    response.build();
    return response;
  }

  @SuppressWarnings("unchecked")
  private Object[] resolveArgsValue(Object[] args, Method method) throws IOException {
    if (args == null) {
      return null;
    }
    Object[] resolvedArgs = new Object[args.length];
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Any && Message.class.isAssignableFrom(parameterTypes[i])) {
        resolvedArgs[i] = ((Any) args[i]).unpack((Class<Message>) parameterTypes[i]);
      } else {
        resolvedArgs[i] = args[i];
      }
    }
    return resolvedArgs;
  }

}
