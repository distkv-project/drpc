package org.dousi.test.benchmark;

import org.dousi.test.common.BenchmarkIService;
import org.dousi.test.common.BenchmarkIServiceImpl;
import com.distkv.drpc.DrpcServer;
import com.distkv.drpc.Proxy;
import com.distkv.drpc.api.Client;
import org.dousi.config.ClientConfig;
import org.dousi.config.ServerConfig;
import com.distkv.drpc.netty.NettyClient;

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
    Client client = new NettyClient(clientConfig);
    client.open();

    Proxy<BenchmarkIService> proxy = new Proxy<>();
    proxy.setInterfaceClass(BenchmarkIService.class);
    BenchmarkIService service = proxy.getService(client);
    BenchmarkGateway gateway = new BenchmarkGateway();
    gateway.start(service);
  }

}
