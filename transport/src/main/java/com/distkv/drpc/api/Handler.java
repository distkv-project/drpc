package com.distkv.drpc.api;


public interface Handler {

  String getServerName();

  Object handle(Object message);

}
