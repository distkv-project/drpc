package org.dousi.registry;

import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.commons.collections4.CollectionUtils;
import org.dousi.common.DousiServiceInstance;
import org.dousi.exception.DousiRuntimeException;
import org.dousi.utils.CheckNotNullUtill;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DistKvNamingService implements NamingService {

  private DousiURL url;
  private String hostPort;
  private String host;
  // Default port is 80;
  private int port = 80;
  private List<DousiServiceInstance> lastInstances = new ArrayList<>();
  private Timer namingServiceTimer;
  private int updateInterval;

  public DistKvNamingService(DousiURL url) {
    CheckNotNullUtill.checkNotNullUtill(url);
    CheckNotNullUtill.checkStringEmptyOrNull(url.getHostsPorts());
    if (url.getHostsPorts() == null) {
      throw new DousiRuntimeException("This DousiURL does not have hosts&ports.");
    }
    this.url = url;
    this.hostPort = url.getHostsPorts();
    String[] hostPort = this.hostPort.split(":");
    this.host = hostPort[0];
    if (hostPort.length == 2) {
      this.port = Integer.valueOf(hostPort[1]);
    }
  }

  @Override
  public List<DousiServiceInstance> pull(String serviceName)
        throws UnknownHostException {
    InetAddress[] inetAddresses = InetAddress.getAllByName(host);
    List<DousiServiceInstance> instances = new ArrayList<>();
    for (InetAddress address : inetAddresses) {
      DousiServiceInstance instance = new DousiServiceInstance(address.getHostAddress(), port);
      instances.add(instance);
    }
    return instances;
  }

  @Override
  public void publish(String address, String interfaceName) {

  }

  @Override
  public void unPublish(String address, String interfaceName) {

  }

  @Override
  public void subscribe(String serviceName, NotifyListener listener) {
    namingServiceTimer.newTimeout(
          new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
              try {
                List<DousiServiceInstance> currentInstances = pull(null);
                Collection<DousiServiceInstance> addList =
                      CollectionUtils.subtract(currentInstances, lastInstances);
                Collection<DousiServiceInstance> removeList =
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
  public void unsubscribe(String serviceName) {

  }

  @Override
  public void destory() {

  }

  public String getHostPort() {
    return this.hostPort;
  }
}
