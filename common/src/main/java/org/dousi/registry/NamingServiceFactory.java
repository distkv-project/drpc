package org.dousi.registry;

public interface NamingServiceFactory {

  String getName();

  NamingService newNamingService(DousiURL url);
}
