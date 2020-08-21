package org.drpc.test.benchmark;

import org.drpc.test.common.BenchmarkIService;
import org.drpc.test.common.BenchmarkIServiceImpl;
import org.drpc.DrpcServer;
import org.drpc.Proxy;
import org.drpc.api.Client;
import org.drpc.config.ClientConfig;
import org.drpc.config.ServerConfig;
import org.drpc.netty.DrpcClient;

public class Benchmark {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .workerThreadNum(256)
        .port(25500)
        .build();

    DrpcServer drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(BenchmarkIService.class, new BenchmarkIServiceImpl());
    drpcServer.run();

    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:25500")
        .build();
    Client client = new DrpcClient(clientConfig);
    client.open();

    Proxy<BenchmarkIService> proxy = new Proxy<>();
    proxy.setInterfaceClass(BenchmarkIService.class);
    BenchmarkIService service = proxy.getService(client);
    BenchmarkGateway gateway = new BenchmarkGateway();
    gateway.start(service);
  }

}
