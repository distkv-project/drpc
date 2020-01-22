package org.dousi.codec;

import org.dousi.api.DefaultResponse;
import org.dousi.api.ProtobufRequestDelegate;
import org.dousi.api.ProtobufResponseDelegate;
import org.dousi.codec.generated.DousiProtocol;
import org.dousi.exception.DousiCodecException;

public class DousiCodec implements Codec {

  public DousiCodec() {

  }

  @Override
  public byte[] encode(Object message) throws DousiCodecException {
    if (message instanceof ProtobufRequestDelegate) {
      ProtobufRequestDelegate request = (ProtobufRequestDelegate) message;
      return request.getDelegatedRequest().toByteArray();
    }
    if (message instanceof ProtobufResponseDelegate) {
      ProtobufResponseDelegate response = (ProtobufResponseDelegate) message;
      return response.getDelegatedResponse().toByteArray();
    }
    if (message instanceof DefaultResponse) {
      ProtobufResponseDelegate response = new ProtobufResponseDelegate();
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
  public Object decode(byte[] data, DataTypeEnum dataTypeEnum) throws DousiCodecException {
    try {
      if (DataTypeEnum.REQUEST == dataTypeEnum) {
        DousiProtocol.Request request = DousiProtocol.Request.parseFrom(data);
        return new ProtobufRequestDelegate(request);
      }
      if (DataTypeEnum.RESPONSE == dataTypeEnum) {
        DousiProtocol.Response response = DousiProtocol.Response.parseFrom(data);
        return new ProtobufResponseDelegate(response);
      }
      throw new DousiCodecException("Illegal DataTypeEnum: " + dataTypeEnum);
    } catch (Exception e) {
      throw new DousiCodecException("Decode error", e);
    }
  }
}
