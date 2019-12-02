package com.distkv.drpc.pb;


import com.distkv.drpc.Reference;
import com.distkv.drpc.pb.generated.StringProtocol;

public class PBClient {

  public static void main(String[] args) throws Throwable {
    Reference<IPBServer> reference = new Reference<>();
    reference.setAddress("dst://127.0.0.1:8080");
    reference.setInterfaceClass(IPBServer.class);
    IPBServer server = reference.getReference();

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

    Reference<IPBServer2> reference2 = new Reference<>();
    reference2.setAddress("dst://127.0.0.1:8080");
    reference2.setInterfaceClass(IPBServer2.class);
    IPBServer2 server2 = reference2.getReference();
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