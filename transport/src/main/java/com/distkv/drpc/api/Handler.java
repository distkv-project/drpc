package com.distkv.drpc.api;


public interface Handler {

  String getServiceName();

  Object handle(Object message);

}
