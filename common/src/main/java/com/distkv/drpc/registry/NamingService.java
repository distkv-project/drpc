package registry;

import common.DrpcServiceInstance;

import java.util.Collection;

public interface NamingService {

  /**
   * Pull eligible registered services;
   *
   * @param info Can be service name\ version number\ group
   * @return Registration service list, could be empty
   */
  Collection<DrpcServiceInstance> pullRegisteredService(SubscribeInfo info);


}
