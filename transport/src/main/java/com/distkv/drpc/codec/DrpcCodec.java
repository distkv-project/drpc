package com.distkv.drpc.codec;

import com.distkv.drpc.api.DefaultResponse;
import com.distkv.drpc.api.PbRequestDelegate;
import com.distkv.drpc.api.PbResponseDelegate;
import com.distkv.drpc.codec.generated.DrpcProtocol;
import com.distkv.drpc.exception.CodecException;

public class DrpcCodec implements Codec {

  private Serialization serialization;

  public DrpcCodec(Serialization serialization) {
    this.serialization = serialization;
  }

  @Override
  public byte[] encode(Object message) throws CodecException {
    if (message instanceof PbRequestDelegate) {
      PbRequestDelegate request = (PbRequestDelegate) message;
      return request.getDelegatedRequest().toByteArray();
    }
    if (message instanceof PbResponseDelegate) {
      PbResponseDelegate response = (PbResponseDelegate) message;
      return response.getDelegatedResponse().toByteArray();
    }
    if (message instanceof DefaultResponse) {
      PbResponseDelegate response = new PbResponseDelegate();
      DefaultResponse defaultResponse = (DefaultResponse) message;
      response.setRequestId(defaultResponse.getRequestId());
      response.setStatus(defaultResponse.getStatus());
      if (defaultResponse.getValue() != null) {
        response.setValue(defaultResponse.getValue());
      } else if (defaultResponse.getThrowable() != null) {
        response.setThrowable(defaultResponse.getThrowable());
      }
      response.build();
      return encode(response);
    }
    throw new IllegalArgumentException();
  }

  @Override
  public Object decode(byte[] data, boolean isRequest) throws CodecException {
    try {
      if (isRequest) {
        DrpcProtocol.Request request = DrpcProtocol.Request.parseFrom(data);
        return new PbRequestDelegate(request);
      } else {
        DrpcProtocol.Response response = DrpcProtocol.Response.parseFrom(data);
        return new PbResponseDelegate(response);
      }
    } catch (Exception e) {
      throw new CodecException("Decode error", e);
    }
  }
}
