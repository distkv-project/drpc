package org.dousi.registry.zookeeper;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.dousi.common.DousiAddress;
import org.dousi.common.DousiServiceInstance;
import org.dousi.registry.DousiConstants;
import org.dousi.registry.DousiURL;
import org.dousi.registry.NamingService;
import org.dousi.registry.NotifyListener;
import org.dousi.utils.CustomThreadFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ZookeeperNamingService implements NamingService {
  protected DousiURL url;
  protected CuratorFramework client;
  private int retryInterval;
  private Timer timer;

  /// <interfaceName, address>
  protected ConcurrentMap<String, String> failedPublish =
        new ConcurrentHashMap<>();
  protected ConcurrentMap<String, String> failedUnpublish =
        new ConcurrentHashMap<>();
  /// <serviceName, NotifyListener>
  protected ConcurrentMap<String, NotifyListener> failedSubscribe =
        new ConcurrentHashMap<>();
  protected ConcurrentSet<String> failedUnsubscribe =
        new ConcurrentSet<>();
  protected ConcurrentMap<String, NotifyListener> subscribeCache =
        new ConcurrentHashMap<>();

  public ZookeeperNamingService(DousiURL url) {
    this.url = url;
    int sleepTimeoutMs = url.getIntParameter(
          DousiConstants.SLEEP_TIME_MS, DousiConstants.DEFAULT_SLEEP_TIME_MS);
    int maxRetryTimes = url.getIntParameter(
          DousiConstants.MAX_RETRY_TIMES, DousiConstants.DEFAULT_MAX_RETRY_TIMES);
    int sessionTimeoutMs = url.getIntParameter(
          DousiConstants.SESSION_TIMEOUT_MS, DousiConstants.DEFAULT_SESSION_TIMEOUT_MS);
    int connectTimeoutMs = url.getIntParameter(
          DousiConstants.CONNECT_TIMEOUT_MS, DousiConstants.DEFAULT_CONNECT_TIMEOUT_MS);

    String namespace = DousiConstants.DEFAULT_PATH;

    if (url.getPath().startsWith("/")) {
      namespace = url.getPath().substring(1);
    }
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(
          sleepTimeoutMs,maxRetryTimes);
    client = CuratorFrameworkFactory.builder()
          .connectString(url.getHostsPorts())
          .connectionTimeoutMs(connectTimeoutMs)
          .sessionTimeoutMs(sessionTimeoutMs)
          .retryPolicy(retryPolicy)
          .namespace(namespace)
          .build();
    client.start();

    this.retryInterval = url.getIntParameter(DousiConstants.INTERVAL, DousiConstants.DEFAULT_INTERVAL);
    timer = new HashedWheelTimer(new CustomThreadFactory("zookeeper-retry-timer-thread"));
    timer.newTimeout(
          new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
              try {
                for (Map.Entry<String, String> entry : failedPublish.entrySet()) {
                  publish(entry.getKey(), entry.getKey());
                }
                for (Map.Entry<String, String> entry : failedUnpublish.entrySet()) {
                  unPublish(entry.getKey(), entry.getValue());
                }
                for (Map.Entry<String, NotifyListener> entry : failedSubscribe.entrySet()) {
                  subscribe(entry.getKey(), entry.getValue());
                }
                for (String serviceName : failedUnsubscribe) {
                  unsubscribe(serviceName);
                }
              } catch (Exception e) {
                log.warn("Retry timer exception:", e);
              }
              timer.newTimeout(this, retryInterval, TimeUnit.MILLISECONDS);
            }
          },
          retryInterval, TimeUnit.MILLISECONDS);
  }

  @Override
  public Collection<DousiServiceInstance> pull(String serviceName) throws UnknownHostException {
    String parentPath = getParentPath(serviceName);
    List<DousiServiceInstance> instanceList = new ArrayList<>();
    try {
      List<String> children = client.getChildren().forPath(parentPath);
      for (String child : children) {
        String childPath = parentPath + "/" + child;
        try {
          String childData = new String(client.getData().forPath(childPath));
          DousiAddress address = new DousiAddress(childData.toString());
          instanceList.add(new DousiServiceInstance(address));
        } catch (Exception getDataFailed) {
          log.warn("Get child data failed, path: {}, ex: ", childPath, getDataFailed);
        }
      }
      log.info("Pull {} instances from {}", instanceList.size(), url);
    } catch (Exception e) {
      log.info("Pull address list failed from {}, msg: ", url);
      //throw new DrpcException("Pull address list failed from zookeeper", e);
    }
    return instanceList;
  }

  @Override
  public void publish(String interfaceName, String address) {
    String parentPath = getParentPath(interfaceName);
    String path = getPath(interfaceName, address);
    try {
      if (client.checkExists().forPath(parentPath) == null) {
        client.create().withMode(CreateMode.PERSISTENT).forPath(parentPath);
      }

      if (client.checkExists().forPath(path) != null) {
        try {
          client.delete().forPath(path);
        } catch (Exception delete) {
          log.warn("Zookeeper delete node {} failed, default ignore.", path);
        }
      }
      client.create().withMode(CreateMode.EPHEMERAL).forPath(path, address.getBytes());
      log.info("Publish success: {}", path);
      System.out.println("Publish success: " + path);
    } catch (Exception e) {
      log.warn("Publish failed: {}", path);
      failedPublish.putIfAbsent(interfaceName, address);
      return;
    }
    failedPublish.remove(interfaceName, address);
  }

  @Override
  public void unPublish(String interfaceName, String address) {
    String path = getPath(interfaceName, address);
    try {
      client.delete().guaranteed().forPath(path);
      log.info("UnPublish success: {}", path);
      System.out.println("UnPublish success: " + path);
    } catch (Exception e) {
      log.warn("UnPublish failed: {}", path);
      failedUnpublish.putIfAbsent(interfaceName, address);
    }
  }

  @Override
  public void subscribe(String serviceName, NotifyListener listener) {

  }

  @Override
  public void unsubscribe(String serviceName) {

  }

  @Override
  public void destory() {

  }

  private String getParentPath(String interfaceName) {
    return "/" + interfaceName;
  }

  private String getPath(String interfaceName, String address) {
    return getParentPath(interfaceName) + "/" + address;
  }
}
