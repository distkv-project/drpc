package org.drpc.test.benchmark;

import org.drpc.DrpcServer;
import org.drpc.Stub;
import org.drpc.api.Client;
import org.drpc.config.ClientConfig;
import org.drpc.config.ServerConfig;
import org.drpc.netty.DrpcClient;
import org.drpc.test.common.BenchmarkIService;
import org.drpc.test.common.BenchmarkIServiceImpl;

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

    Stub<BenchmarkIService> stub = new Stub<>(BenchmarkIService.class);
    BenchmarkIService service = stub.getService(client);
    BenchmarkGateway gateway = new BenchmarkGateway();
    gateway.start(service);
  }

}
