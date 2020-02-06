package org.dousi.registry;

import org.dousi.common.DousiServiceInstance;

import java.util.Collection;

public interface NotifyListener {

  /**
   * Alert when service changes;
   *
   * @param addList Need to be added DousiAddress;
   * @param removeList Need to be removed DousiAddress;
   */
  void notify(Collection<DousiServiceInstance> addList,
              Collection<DousiServiceInstance> removeList);
}
