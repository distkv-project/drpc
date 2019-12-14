package com.distkv.drpc.benchmark.dubbo;

import java.util.HashMap;
import java.util.Map;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

public class DubboProvider {

  private static ApplicationConfig application = new ApplicationConfig();
  private static RegistryConfig registry = new RegistryConfig();
  private static ProtocolConfig protocol = new ProtocolConfig();

  public static void main(String[] args) throws InterruptedException {
    application.setName("dubbo-provider");
    application.setQosEnable(false);

    registry.setAddress("N/A");

    protocol.setName("dubbo");
    protocol.setPort(25500);
    protocol.setThreads(300);
    protocol.setHost("0.0.0.0");

    exportHashService();
    Thread.currentThread().join();
  }

  private static void exportHashService() {
    ServiceConfig<IService> service =
        new ServiceConfig<>();
    Map<String, String> attributes = new HashMap<>();
    attributes.put("heartbeat", "0");
    service.setParameters(attributes);
    service.setApplication(application);
    service.setRegistry(registry);
    service.setProtocol(protocol);
    service.setInterface(IService.class);
    service.setRef(new IServiceImpl());
    service.export();
  }

}
