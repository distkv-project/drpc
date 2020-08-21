package org.drpc.api;


public interface Handler {

  String getServiceName();

  Object handle(Object message);

}
