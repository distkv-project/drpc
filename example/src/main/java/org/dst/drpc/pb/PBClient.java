package org.dst.drpc.pb;


import org.dst.drpc.Reference;
import org.dst.drpc.pb.generated.StringProtocol;


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

    System.out.println("-------------------------------------------------------");

    // put
    StringProtocol.PutRequest putRequest = StringProtocol.PutRequest.newBuilder()
          .setKey("dstPut")
          .setValue("PutValue")
          .build();
    StringProtocol.PutResponse putResponse = server.put(putRequest).get();
    System.out.println(putResponse.getStatus());
  }
}
