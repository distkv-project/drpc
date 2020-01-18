package registry;

import exception.DrpcIllegalUrlException;

public class DistKvNamingServiceFactory implements NamingServiceFactory {

  @Override
  public String getName() {
    return "distkv";
  }

  @Override
  public NamingService newNamingService(DrpcURL url) {
    String protocol = url.getProtocol();
    if ("distkv".equals(protocol)) {
      return new DistKvNamingService(url);
    } else {
      throw new DrpcIllegalUrlException("Illegal url protocol: " + url.toString());
    }
  }
}
