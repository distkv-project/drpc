package utils;

import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CustomThreadFactory implements ThreadFactory {

  private AtomicInteger threadNumber = new AtomicInteger(1);
  private String namePrefix;
  private ThreadGroup group;

  public CustomThreadFactory(String namePrefix) {
    SecurityManager securityManager = System.getSecurityManager();
    this.group = (securityManager != null) ? securityManager.getThreadGroup() :
          Thread.currentThread().getThreadGroup();
    this.namePrefix = namePrefix + "-";
  }

  @Override
  public Thread newThread(Runnable runnable) {
    String name = namePrefix + threadNumber.getAndIncrement();
    Thread thread = new FastThreadLocalThread(group, runnable, name, 0);
    thread.setDaemon(true);
    thread.setPriority(Thread.NORM_PRIORITY);
    log.info("Create thread:{}", name);
    return thread;
  }
}
