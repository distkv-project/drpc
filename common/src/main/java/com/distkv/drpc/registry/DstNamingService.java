package registry;

import common.DrpcServiceInstance;

import java.util.Collection;

public class DstNamingService implements NamingService {
  @Override
  public Collection<DrpcServiceInstance> pullRegisteredService(SubscribeInfo info) {
    return null;
  }

  @Override
  public void register(RegisterInfo info) {

  }

  @Override
  public void unregister(RegisterInfo info) {

  }

  @Override
  public void subscribe(SubscribeInfo info, NotifyListener listener) {

  }

  @Override
  public void unsubscribe(SubscribeInfo info) {

  }

  @Override
  public void destory() {

  }
}
