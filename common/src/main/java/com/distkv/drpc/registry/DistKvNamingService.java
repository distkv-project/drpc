package registry;

import com.distkv.drpc.exception.DrpcRuntimeException;
import common.DrpcServiceInstance;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.commons.collections4.CollectionUtils;
import utils.CheckNotNullUtill;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DistKvNamingService implements NamingService {

  private DrpcURL url;
  private String hostPort;
  private String host;
  // Default port is 80;
  private int port = 80;
  private List<DrpcServiceInstance> lastInstances = new ArrayList<>();
  private Timer namingServiceTimer;
  private int updateInterval;

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
  public List<DrpcServiceInstance> pullRegisteredService(SubscribeInfo info)
        throws UnknownHostException {
    InetAddress[] inetAddresses;
    inetAddresses = InetAddress.getAllByName(host);
    List<DrpcServiceInstance> instances = new ArrayList<>();
    for (InetAddress address : inetAddresses) {
      DrpcServiceInstance instance = new DrpcServiceInstance(address.getHostAddress(), port);
      instances.add(instance);
    }
    return instances;
  }

  @Override
  public void register(RegisterInfo info) {

  }

  @Override
  public void unregister(RegisterInfo info) {

  }

  @Override
  public void subscribe(SubscribeInfo info, NotifyListener listener) {
    namingServiceTimer.newTimeout(
          new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
              try {
                List<DrpcServiceInstance> currentInstances = pullRegisteredService(null);
                Collection<DrpcServiceInstance> addList =
                      CollectionUtils.subtract(currentInstances, lastInstances);
                Collection<DrpcServiceInstance> removeList =
                      CollectionUtils.subtract(lastInstances, currentInstances);
                listener.notify(addList, removeList);
                lastInstances = currentInstances;
              } catch (Exception ex) {
                //ignore
              }
              namingServiceTimer.newTimeout(this, updateInterval, TimeUnit.MILLISECONDS);
            }
          }, updateInterval, TimeUnit.MILLISECONDS);
  }

  @Override
  public void unsubscribe(SubscribeInfo info) {

  }

  @Override
  public void destory() {

  }

  public String getHostPort() {
    return this.hostPort;
  }
}
