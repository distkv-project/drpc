package com.distkv.drpc.pb;


import com.distkv.drpc.Reference;
import com.distkv.drpc.api.Client;
import com.distkv.drpc.config.ClientConfig;
import com.distkv.drpc.pb.generated.StringProtocol;

public class PBClient {

  public static void main(String[] args) throws Throwable {
    ClientConfig clientConfig = ClientConfig.builder()
        .address("127.0.0.1:8080")
        .build();

    Reference<IPBService> reference = new Reference<>(clientConfig);
    reference.setInterfaceClass(IPBService.class);
    IPBService server = reference.getReference();

    //get
    StringProtocol.GetRequest getRequest = StringProtocol.GetRequest.newBuilder()
            .setKey("dstGet").build();
    StringProtocol.GetResponse getResponse = server.get(getRequest).get();
    System.out.println(getResponse.getStatus());
    System.out.println(getResponse.getValue());

    // put
    StringProtocol.PutRequest putRequest = StringProtocol.PutRequest.newBuilder()
            .setKey("dstPut")
            .setValue("PutValue")
            .build();
    StringProtocol.PutResponse putResponse = server.put(putRequest).get();
    System.out.println(putResponse.getStatus());

    System.out.println("-------------------------------------------------------");

    Reference<IPBService2> reference2 = new Reference<>(clientConfig);
    reference2.setInterfaceClass(IPBService2.class);
    IPBService2 server2 = reference2.getReference();
    //get2
    StringProtocol.GetRequest getRequest2 = StringProtocol.GetRequest.newBuilder()
            .setKey("dstGet2").build();
    StringProtocol.GetResponse getResponse2 = server2.get2(getRequest2).get();
    System.out.println(getResponse2.getStatus());
    System.out.println(getResponse2.getValue());

    // put2
    StringProtocol.PutRequest putRequest2 = StringProtocol.PutRequest.newBuilder()
            .setKey("dstPut2")
            .setValue("PutValue2")
            .build();
    StringProtocol.PutResponse putResponse2 = server2.put2(putRequest2).get();
    System.out.println(putResponse2.getStatus());

  }
}
