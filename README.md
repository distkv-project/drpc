# Drpc
[![codecov](https://codecov.io/gh/distkv-project/drpc/branch/master/graph/badge.svg)](https://codecov.io/gh/dist/drpc)

`Drpc` is a high performance RPC framework, which aims to help users build their networking communications easily.
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
Server example: ./example/src/main/java/com/distkv/drpc/pb/ExampleServer.java
```
### 3. Run Client Example
```bash
Client example: ./example/src/main/java/com/distkv/drpc/pb/ExampleServer.java
```
## Examples
### 1. Server Example
```java
import org.drpc.DrpcServer;
import org.drpc.config.ServerConfig;

public class ExampleServer {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(8080)
        .build();

    DrpcServer drpcServer = new DrpcServer(serverConfig);
    drpcServer.registerService(ExampleService.class, new ExampleServiceImpl());
    drpcServer.run();
  }
}
```
### 2. Client Example
```java
import org.drpc.Stub;
import org.drpc.api.Client;
import org.drpc.config.ClientConfig;
import org.drpc.netty.DrpcClient;
import org.drpc.pb.generated.StringProtocol;
import org.drpc.session.DrpcSession;

import java.util.concurrent.CompletableFuture;

public class ExampleClient {

  public static void main(String[] args) throws Throwable {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    Client client = new DrpcClient(clientConfig);
    client.open();

    Stub<ExampleService> stub = new Stub<>(ExampleService.class);
    ExampleService service = stub.getService(client);

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


    //session (keep order)
    DrpcSession session = DrpcSession.createSession();
    ExampleService sessionService = stub.getService(client, session);

    //async (keep order in server)
    CompletableFuture sessionFuture1 = sessionService.get(getRequest);
    sessionFuture1.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(getResponse.getValue());
      }
    });
    CompletableFuture sessionFuture2 = sessionService.put(putRequest);
    sessionFuture2.whenComplete((r, t) -> {
      if (t == null) {
        System.out.println(putResponse.getStatus());
      }
    });

    sessionFuture1.get();
    sessionFuture2.get();

    client.close();
  }
}
```
## Roadmap

#### Done
- ~~Asynchronous services in server~~
- ~~Order preserving in one client context~~
- ~~Session concept for Order preserving~~

#### Doing
- Go client and C++ client
- Dynamic registration for service

#### Planned and TBD
- Support Multiple languages for server
- Load balance for services

## Performance
TODO

## Getting Involved
Thank you for your attention to the `Drpc` project. If you have any questions, you can create a new issue in our Issues list. And we welcome you to participate in our project. If you want to make some contributions, you can refer the file [CONTRIBUTING.md](https://github.com/dst-project/drpc/blob/master/CONTRIBUTING.md).
