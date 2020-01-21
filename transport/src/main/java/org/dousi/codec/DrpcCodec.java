package org.dousi.codec;

import org.dousi.api.DefaultResponse;
import org.dousi.api.ProtobufRequestDelegate;
import org.dousi.api.ProtobufResponseDelegate;
import com.distkv.drpc.codec.generated.DrpcProtocol;
import org.dousi.exception.CodecException;

public class DrpcCodec implements Codec {

  public DrpcCodec() {

  }

  @Override
  public byte[] encode(Object message) throws CodecException {
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
  public Object decode(byte[] data, DataTypeEnum dataTypeEnum) throws CodecException {
    try {
      if (DataTypeEnum.REQUEST == dataTypeEnum) {
        DrpcProtocol.Request request = DrpcProtocol.Request.parseFrom(data);
        return new ProtobufRequestDelegate(request);
      }
      if (DataTypeEnum.RESPONSE == dataTypeEnum) {
        DrpcProtocol.Response response = DrpcProtocol.Response.parseFrom(data);
        return new ProtobufResponseDelegate(response);
      }
      throw new CodecException("Illegal DataTypeEnum: " + dataTypeEnum);
    } catch (Exception e) {
      throw new CodecException("Decode error", e);
    }
  }
}
