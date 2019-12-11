package com.distkv.drpc.test.codec;

import com.distkv.drpc.codec.generated.DrpcProtocol;
import com.distkv.drpc.pb.generated.StringProtocol;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProtoBufTest {

  @Test
  public void testAny() {

    String testKey = "key";

    StringProtocol.GetRequest getRequest = StringProtocol.GetRequest.newBuilder()
        .setKey(testKey)
        .build();

    DrpcProtocol.Request rpcRequest = DrpcProtocol.Request.newBuilder()
        .setRequestId(1)
        .setInterfaceName("IService")
        .setMethodName("get")
        .addArgs(Any.pack(getRequest))
        .build();

    // serialize
    byte[] bytes = rpcRequest.toByteArray();

    // deserialize
    try {
      DrpcProtocol.Request request = DrpcProtocol.Request.parseFrom(bytes);
      Assert.assertTrue(request.getArgs(0).is(StringProtocol.GetRequest.class));
      StringProtocol.GetRequest getRequest1 = request.getArgs(0)
          .unpack(StringProtocol.GetRequest.class);
      Assert.assertEquals(getRequest1.getKey(), testKey);
    } catch (InvalidProtocolBufferException e) {
      Assert.fail("Deserialize failed");
    }
  }

}
