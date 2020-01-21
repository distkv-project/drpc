package org.dousi.dousi.api;


public interface Handler {

  String getServiceName();

  Object handle(Object message);

}
