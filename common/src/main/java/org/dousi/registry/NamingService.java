package org.dousi.registry;

import org.dousi.common.DousiServiceInstance;
import java.net.UnknownHostException;
import java.util.Collection;

public interface NamingService {
  /**
   * Subscribe and publish may return results for this operation;
   */

  /**
   * Pull eligible registered services;
   *
   * @param serviceName Name of the published service, can be empty;
   * @return Registration service list, could be empty;
   */
  Collection<DousiServiceInstance> pull(String serviceName) throws UnknownHostException;

  /**
   * Register for a service;
   *
   * @param address Server address;
   * @param interfaceName Name of the published interface, can't be empty;
   */
  void publish(String interfaceName, String address);

  /**
   * Unregister for a service;
   *
   * @param interfaceName Name of the published interface, can't be empty;
   */
  void unPublish(String interfaceName, String address);

  /**
   * Subscribe to a registered service;
   *
   * @param serviceName Can be service name\ version number\ group
   * @param listener Service change listener
   */
  void subscribe(String serviceName, NotifyListener listener);

  /**
   * Unsebscribe a service;
   *
   * @param serviceName Can be service name\ version number\ group
   */
  void unsubscribe(String serviceName);

  void destory();
}
