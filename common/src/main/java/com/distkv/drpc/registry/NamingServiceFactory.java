package registry;

public interface NamingServiceFactory {

  String getName();

  NamingService newNamingService(DrpcURL url);
}
