package org.dousi.registry;

import org.dousi.exception.DousiIllegalUrlException;

public class DistKvNamingServiceFactory implements NamingServiceFactory {

  @Override
  public String getName() {
    return "distkv";
  }

  @Override
  public NamingService newNamingService(DousiURL url) {
    String protocol = url.getProtocol();
    if ("distkv".equals(protocol)) {
      return new DistKvNamingService(url);
    } else {
      throw new DousiIllegalUrlException("Illegal url protocol: " + url.toString());
    }
  }
}
