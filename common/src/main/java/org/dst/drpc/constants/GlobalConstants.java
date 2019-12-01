package org.dst.drpc.constants;


public interface GlobalConstants {

  int threadNumber = Runtime.getRuntime().availableProcessors();

  int DEFAULT_CLIENT_TIMEOUT = 3000; // 3 second

}
