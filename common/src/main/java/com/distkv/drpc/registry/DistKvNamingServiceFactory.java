package registry;

import exception.DrpcIllegalUrlException;

public class DistKvNamingServiceFactory implements NamingServiceFactory {

  @Override
  public String getName() {
    return "dist";
  }

  @Override
  public NamingService newNamingService(DrpcURL url) {
    String protocol = url.getProtocol;
    if ("dist".equals(protocol)) {
      return new DistKvNamingService(url);
    } else {
      throw new DrpcIllegalUrlException("Illegal url protocol: " + url.toString());
    }
    return null;
  }
}
