package registry;

import common.DrpcServiceInstance;

import java.net.UnknownHostException;
import java.util.Collection;

public interface NamingService {
  /**
   * Subscribe and register may return results for this operation;
   */

  /**
   * Pull eligible registered services;
   *
   * @param info Can be service name\ version number\ group
   * @return Registration service list, could be empty
   */
  Collection<DrpcServiceInstance> pullRegisteredService(SubscribeInfo info) throws UnknownHostException;

  /**
   * Register for a service;
   *
   * @param info Can be service name\ version number\ group
   */
  void register(RegisterInfo info);

  /**
   * Unregister for a service;
   *
   * @param info Can be service name\ version number\ group
   */
  void unregister(RegisterInfo info);

  /**
   * Subscribe to a registered service;
   *
   * @param info Can be service name\ version number\ group
   * @param listener Service change listener
   */
  void subscribe(SubscribeInfo info, NotifyListener listener);

  /**
   * Unsebscribe a service;
   *
   * @param info Can be service name\ version number\ group
   */
  void unsubscribe(SubscribeInfo info);

  void destory();
}
