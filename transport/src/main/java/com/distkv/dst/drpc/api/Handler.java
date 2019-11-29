package com.distkv.dst.drpc.api;


public interface Handler {

  String getServerName();

  Object handle(Object message);

}
