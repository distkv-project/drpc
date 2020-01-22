package org.dousi.test.benchmark;

import org.dousi.test.common.BenchmarkIService;
import org.dousi.test.common.BenchmarkIServiceImpl;
import org.dousi.DousiServer;
import org.dousi.Proxy;
import org.dousi.api.Client;
import org.dousi.config.ClientConfig;
import org.dousi.config.ServerConfig;
import org.dousi.netty.NettyClient;

public class Benchmark {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .workerThreadNum(256)
        .port(25500)
        .build();

    DousiServer drpcServer = new DousiServer(serverConfig);
    drpcServer.registerService(BenchmarkIService.class, new BenchmarkIServiceImpl());
    drpcServer.run();

    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:25500")
        .build();
    Client client = new NettyClient(clientConfig);
    client.open();

    Proxy<BenchmarkIService> proxy = new Proxy<>();
    proxy.setInterfaceClass(BenchmarkIService.class);
    BenchmarkIService service = proxy.getService(client);
    BenchmarkGateway gateway = new BenchmarkGateway();
    gateway.start(service);
  }

}
