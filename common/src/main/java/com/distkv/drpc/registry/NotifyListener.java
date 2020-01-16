package registry;

import common.DrpcServiceInstance;

import java.util.Collection;

public interface NotifyListener {

  /**
   * Alert when service changes;
   *
   * @param addList Need to be added DrpcAddress;
   * @param removeList Need to be removed DrpcAddress;
   */
  void notify(Collection<DrpcServiceInstance> addList,
              Collection<DrpcServiceInstance> removeList);
}
