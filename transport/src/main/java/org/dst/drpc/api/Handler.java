package org.dst.drpc.api;


public interface Handler {

  String getServerName();

  Object handle(Object message);

}
