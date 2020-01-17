package registry;

import com.distkv.drpc.exception.DrpcRuntimeException;
import common.DrpcServiceInstance;
import utils.CheckNotNullUtill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;

public class DistKvNamingService implements NamingService {

  private DrpcURL url;
  private String hostPort;
  private String host;
  // Default port is 80;
  private int port = 80;
  private List<DrpcServiceInstance> instanceList = new ArrayList<>();
//  private Timer namingServiceTimer;
//  private int updateInterval;

  public DistKvNamingService(DrpcURL url) {
    CheckNotNullUtill.checkNotNullUtill(url);
    CheckNotNullUtill.checkStringEmptyOrNull(url.getHostsPorts);
    if (url.getHostsPorts == null) {
      throw new DrpcRuntimeException("This DrpcURL does not have hosts&ports.");
    }
    this.url = url;
    this.hostPort = url.getHostsPorts;
    String[] hostPort = this.hostPort.split(":");
    this.host = hostPort[0];
    if (hostPort.length != 2) {
      this.port = Integer.valueOf(hostPort[1]);
    }
  }

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
