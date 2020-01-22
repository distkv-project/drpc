# Dousi
[![Build Status](https://travis-ci.com/jovany-wang/dousi.svg?branch=master)](https://travis-ci.com/jovany-wang/dousi)
[![codecov](https://codecov.io/gh/jovany-wang/dousi/branch/master/graph/badge.svg)](https://codecov.io/gh/jovany-wang/dousi)
[![maven](https://img.shields.io/maven-central/v/com.distkv/dousi.svg)](https://search.maven.org/search?q=com.distkv)

The next generation RPC framework.
## Project Description
`Dousi` is a high performance RPC framework, which aims to help users build their networking communications easily.
The biggest feature to distinguish with other RPC frameworks is it supports multiple languages server.

## Awesome Features
- Multiple languages server and client
- Asynchronous services in server
- Dynamic registration for service
- The ability for order preserving
- High performance and easy to use

## Quick Started
### 1. Build Project
```bash
mvn clean install -DskipTests
```
### 2. Run Server Example
```bash
Server example: ./example/src/main/java/com/distkv/dousi/pb/ExampleServer.java
```
### 3. Run Client Example
```bash
Client example: ./example/src/main/java/com/distkv/dousi/pb/ExampleServer.java
```
## Examples
### 1. Server Example
```java
import org.dousi.DousiServer;
import org.dousi.config.ServerConfig;

public class ExampleServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DousiServer dousiServer = new DousiServer(serverConfig);
    dousiServer.registerService(ExampleService.class, new ExampleServiceImpl());
    dousiServer.run();
  }
}
```
### 2. Client Example
```java
import org.dousi.Proxy;
import org.dousi.api.Client;
import org.dousi.config.ClientConfig;
import org.dousi.netty.NettyClient;
import org.dousi.pb.generated.StringProtocol;

import java.util.concurrent.CompletableFuture;

public class ExampleClient {

  public static void main(String[] args) throws Throwable {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    Client client = new NettyClient(clientConfig);
    client.open();
    Proxy<ExampleService> proxy = new Proxy<>();
    proxy.setInterfaceClass(ExampleService.class);
    ExampleService service = proxy.getService(client);

    StringProtocol.PutRequest putRequest = StringProtocol.PutRequest.newBuilder()
        .setKey("dstPut")
        .setValue("PutValue")
        .build();

    StringProtocol.GetRequest getRequest = StringProtocol.GetRequest.newBuilder()
        .setKey("dstGet").build();

    //sync
    StringProtocol.GetResponse getResponse = service.get(getRequest).get();
    System.out.println(getResponse.getStatus());
    System.out.println(getResponse.getValue());
    StringProtocol.PutResponse putResponse = service.put(putRequest).get();
    System.out.println(putResponse.getStatus());

    //async
    CompletableFuture future1 = service.get(getRequest);
    future1.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(getResponse.getStatus());
        System.out.println(getResponse.getValue());
      }
    });

    CompletableFuture future2 = service.put(putRequest);
    future2.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(putResponse.getStatus());
      }
    });

    client.close();
  }
}
```
## Roadmap

#### Done
- ~~Asynchronous services in server~~
- ~~Order preserving in one client context~~

#### Doing
- Go client and C++ client
- Session concept for Order preserving
- Dynamic registration for service

#### Planned and TBD
- Support Multiple languages for server
- Load balance for services

## Performance
TODO

## Getting Involved
Thank you for your attention to the `Dousi` project. If you have any questions, you can create a new issue in our Issues list. And we welcome you to participate in our project. If you want to make some contributions, you can refer the file [CONTRIBUTING.md](https://github.com/dst-project/dousi/blob/master/CONTRIBUTING.md).
