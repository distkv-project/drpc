package org.dst.drpc.api.async;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.dst.drpc.exception.DrpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultAsyncResponse extends CompletableFuture<Response> implements AsyncResponse {

  private static final Logger logger = LoggerFactory.getLogger(DefaultAsyncResponse.class);

  private Response response;

  public DefaultAsyncResponse(long requestId) {
    this.response = new DefaultResponse(requestId);
  }

  @Override
  public long getRequestId() {
    return response.getRequestId();
  }

  @Override
  public void setRequestId(long requestId) {
    response.setRequestId(requestId);
  }

  @Override
  public Object getValue() {
    return getDefaultResponse().getValue();
  }

  @Override
  public void setValue(Object value) {
    try {
      if (this.isDone()) {
        this.get().setValue(value);
      } else {
        response.setValue(value);
        super.complete(response);
      }
    } catch (Exception e) {
      // This should not happen
      throw new DrpcException(e);
    }
  }

  @Override
  public Throwable getThrowable() {
    return getDefaultResponse().getThrowable();
  }

  @Override
  public void setThrowable(Throwable throwable) {
    try {
      if (this.isDone()) {
        this.get().setThrowable(throwable);
      } else {
        response.setThrowable(throwable);
        super.complete(response);
      }
    } catch (Exception e) {
      // This should not happen
      throw new DrpcException(e);
    }
  }

  @Override
  public boolean hasAttribute(String key) {
    return response.hasAttribute(key);
  }

  @Override
  public Object getAttribute(String key) {
    return response.getAttribute(key);
  }

  @Override
  public void setAttribute(String key, Object value) {
    response.setAttribute(key, value);
  }

  @Override
  public void removeAttribute(String key) {
    response.removeAttribute(key);
  }

  @Override
  public void await() throws InterruptedException, ExecutionException {
    get();
  }

  @Override
  public void await(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    get(timeout, unit);
  }

  @Override
  public boolean complete(Response value) {
    throw new UnsupportedOperationException();
  }

  private void checkDone() {
    if (!isDone()) {
      throw new DrpcException("Must check 'isDone()' first");
    }
  }

  public boolean isDone() {
    return super.isDone();
  }

  private Response getDefaultResponse() {
    try {
      return get();
    } catch (Exception e) {
      // This should not happen
      throw new DrpcException(e);
    }
  }


}
