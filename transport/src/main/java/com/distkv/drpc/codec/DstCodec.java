package com.distkv.drpc.codec;

import com.distkv.drpc.api.DefaultResponse;
import com.distkv.drpc.api.Request;
import com.distkv.drpc.api.Response;
import com.distkv.drpc.common.Void;
import com.distkv.drpc.constants.CodecConstants;
import com.distkv.drpc.exception.CodecException;
import com.distkv.drpc.utils.ReflectUtils;
import java.io.IOException;
import java.io.ObjectInput;
import java.nio.charset.StandardCharsets;

/**
 * protocol header：
 *
 * |      magic 16bit     | version 8bit | type flag 8bit | |               content length 32 bit |
 * |               request id     64 bit                  | |               payload ... |
 */
public class DstCodec implements Codec {

  private Serialization serialization;

  public DstCodec(Serialization serialization) {
    this.serialization = serialization;
  }

  @Override
  public byte[] encode(Object message) throws CodecException {
    if (!(message instanceof Request) && !(message instanceof Response)) {
      throw new CodecException("Unsupported message type when encode: " + message.getClass());
    }

    try {
      if (message instanceof Request) {
        return encodeRequest((Request) message);
      } else {
        return encodeResponse((Response) message);
      }
    } catch (Exception e) {
      throw new CodecException("Encode error: ", e);
    }
  }

  @Override
  public Object decode(byte[] data) throws CodecException {
    int pos = 0;

    short magic = bytesToShort(data, pos);
    if (magic != CodecConstants.MAGIC_HEAD) {
      throw new CodecException("Magic error: " + magic);
    }
    pos += 2;

    byte version = data[pos++];
    byte dataType = data[pos++];

    int contentLength = bytesToInt(data, pos);
    pos += 4;

    long requestId = bytesToLong(data, pos);
    pos += 8;

    byte[] content = new byte[contentLength];
    System.arraycopy(data, pos, content, 0, contentLength);

    try {
      switch (CodecConstants.DataType.getDataTypeByValue(dataType)) {
        case NONE:
          return Void.getInstance();
        case REQUEST:
          return decodeRequest(requestId, content);
        case RESPONSE:
          return decodeResponse(requestId, content);
        case EXCEPTION:
          return decodeException(requestId, content);
        default:
          throw new CodecException("Unknown data type: " + dataType);
      }
    } catch (Exception e) {
      throw new CodecException("Decode from 'byte[] content' error", e);
    }
  }


  private byte[] encodeRequest(Request request) throws Exception {
    byte[] interfaceName = request.getInterfaceName().getBytes(StandardCharsets.UTF_8);
    byte[] methodName = request.getMethodName().getBytes(StandardCharsets.UTF_8);
    byte[][] argsArray = null;
    int argsNumber = request.getArgsValue() == null ? 0 : request.getArgsValue().length;
    int argsLength = 0;
    if (argsNumber > 0) {
      argsArray = new byte[argsNumber][];
      for (int i = 0; i < argsNumber; i++) {
        argsArray[i] = serialization.serialize(request.getArgsValue()[i]);
        argsLength += argsArray[i].length;
      }
    }

    int bodyLength = interfaceName.length + 2 // interfaceName
        + methodName.length + 2 // methodName
        + 2 // number of args
        + argsNumber * 4 // args length
        + argsLength;

    int pos = 0;
    byte[] body = new byte[bodyLength];

    shortToBytes(body, pos, (short) interfaceName.length);
    pos += 2;
    System.arraycopy(interfaceName, 0, body, pos, interfaceName.length);
    pos += interfaceName.length;

    shortToBytes(body, pos, (short) methodName.length);
    pos += 2;
    System.arraycopy(methodName, 0, body, pos, methodName.length);
    pos += methodName.length;

    shortToBytes(body, pos, (short) argsNumber);
    pos += 2;

    for (int i = 0; i < argsNumber; i++) {
      byte[] arg = argsArray[i];
      intToBytes(body, pos, arg.length);
      pos += 4;
      System.arraycopy(arg, 0, body, pos, arg.length);
      pos += arg.length;
    }

    return encode0(body, CodecConstants.DataType.REQUEST, request.getRequestId());
  }

  private byte[] encodeResponse(Response response) throws Exception {
    CodecConstants.DataType dataType;

    if (response.getThrowable() != null) {
      dataType = CodecConstants.DataType.EXCEPTION;

      byte[] throwableName = response.getThrowable().getClass().getName()
          .getBytes(StandardCharsets.UTF_8);
      byte[] data = serialization.serialize(response.getThrowable());

      int bodyLength = throwableName.length + 2 // name length
          + data.length + 4;

      int pos = 0;
      byte[] body = new byte[bodyLength];
      shortToBytes(body, pos, (short) throwableName.length);
      pos += 2;
      System.arraycopy(throwableName, 0, body, pos, throwableName.length);
      pos += throwableName.length;

      intToBytes(body, pos, data.length);
      pos += 4;
      System.arraycopy(data, 0, body, pos, data.length);

      return encode0(body, dataType, response.getRequestId());
    } else {
      dataType = CodecConstants.DataType.RESPONSE;
      byte[] valueName = response.getValue().getClass().getName()
          .getBytes(StandardCharsets.UTF_8);
      byte[] data = serialization.serialize(response.getValue());

      int bodyLength = valueName.length + 2 // name length
          + data.length + 4;

      int pos = 0;
      byte[] body = new byte[bodyLength];
      shortToBytes(body, pos, (short) valueName.length);
      pos += 2;
      System.arraycopy(valueName, 0, body, pos, valueName.length);
      pos += valueName.length;

      intToBytes(body, pos, data.length);
      pos += 4;
      System.arraycopy(data, 0, body, pos, data.length);

      return encode0(body, dataType, response.getRequestId());
    }
  }

  private byte[] encode0(byte[] body, CodecConstants.DataType dataType, long requestId)
      throws IOException {
    byte[] header = new byte[CodecConstants.HEADER_LENGTH];
    int pos = 0;

    shortToBytes(header, pos, CodecConstants.MAGIC_HEAD);
    pos += 2;

    header[pos++] = CodecConstants.Version.VERSION_1.getValue();
    header[pos++] = dataType.getValue();

    intToBytes(header, pos, body.length);
    pos += 4;

    longToBytes(header, pos, requestId);
    pos += 8;

    byte[] data = new byte[header.length + body.length];
    System.arraycopy(header, 0, data, 0, header.length);
    System.arraycopy(body, 0, data, pos, body.length);

    return data;
  }


  private Object decodeRequest(long requestId, byte[] content) throws Exception {
    int pos = 0;
    byte[] interfaceName = new byte[bytesToShort(content, pos)];
    pos += 2;
    System.arraycopy(content, pos, interfaceName, 0, interfaceName.length);
    pos += interfaceName.length;

    byte[] methodName = new byte[bytesToShort(content, pos)];
    pos += 2;
    System.arraycopy(content, pos, methodName, 0, methodName.length);
    pos += methodName.length;

    Request request = new Request();
    request.setRequestId(requestId);
    request.setInterfaceName(new String(interfaceName));
    request.setMethodName(new String(methodName));

    int argsNumber = bytesToShort(content, pos);
    pos += 2;

    if (argsNumber > 0) {
      Object[] arg = new Object[argsNumber];
      for (int i = 0; i < argsNumber; i++) {
        int dataLength = bytesToInt(content, pos);
        pos += 4;
        byte[] argData = new byte[dataLength];
        System.arraycopy(content, pos, argData, 0, dataLength);
        pos += dataLength;
        DelayDeserialization delayDeserialization = new DelayDeserialization(serialization,
            argData);
        arg[i] = delayDeserialization;
      }
      request.setArgsValue(arg);
    }

    return request;
  }

  private Object decodeResponse0(long requestId, byte[] content, boolean isException)
      throws Exception {
    Response response = new DefaultResponse();

    int pos = 0;
    byte[] className = new byte[bytesToShort(content, pos)];
    pos += 2;
    System.arraycopy(content, pos, className, 0, className.length);
    pos += className.length;

    int dataLength = bytesToInt(content, pos);
    pos += 4;
    byte[] data = new byte[dataLength];
    System.arraycopy(content, pos, data, 0, dataLength);
    Class<?> clz = ReflectUtils.forName(new String(className));
    Object result = serialization.deserialize(data, clz);

    response.setRequestId(requestId);
    if (isException) {
      response.setThrowable((Exception) result);
    } else {
      response.setValue(result);
    }

    return response;
  }

  private Object decodeResponse(long requestId, byte[] content) throws Exception {
    return decodeResponse0(requestId, content, false);
  }

  private Object decodeException(long requestId, byte[] content) throws Exception {
    return decodeResponse0(requestId, content, true);
  }

  private Object[] decodeRequestParameter(ObjectInput input, String parameterDesc)
      throws IOException,
      ClassNotFoundException {
    if (parameterDesc == null || parameterDesc.equals("")) {
      return null;
    }
    Class<?>[] classTypes = ReflectUtils.forNames(parameterDesc);
    Object[] paramObjects = new Object[classTypes.length];
    for (int i = 0; i < classTypes.length; i++) {
      paramObjects[i] = serialization.deserialize((byte[]) input.readObject(), classTypes[i]);
    }
    return paramObjects;
  }

  private short bytesToShort(byte[] data, int pos) {
    return (short) (((data[pos + 1] & 0xFF)) + ((data[pos] & 0xFF) << 8));
  }

  private int bytesToInt(byte[] data, int pos) {
    return ((data[pos] & 0xFF) << 24)
        + ((data[pos + 1] & 0xFF) << 16)
        + ((data[pos + 2] & 0xFF) << 8)
        + (data[pos + 3] & 0xFF);
  }

  private long bytesToLong(byte[] data, int pos) {
    return ((data[pos] & 0xFFL) << 56)
        + ((data[pos + 1] & 0xFFL) << 48)
        + ((data[pos + 2] & 0xFFL) << 40)
        + ((data[pos + 3] & 0xFFL) << 32)
        + ((data[pos + 4] & 0xFFL) << 24)
        + ((data[pos + 5] & 0xFFL) << 16)
        + ((data[pos + 6] & 0xFFL) << 8)
        + (data[pos + 7] & 0xFFL);
  }

  private void shortToBytes(byte[] dst, int pos, short val) {
    dst[pos + 1] = (byte) (val & 0xff);
    dst[pos + 0] = (byte) ((val >> 8) & 0xff);
  }

  private void intToBytes(byte[] dst, int pos, int val) {
    dst[pos + 3] = (byte) (val & 0xff);
    dst[pos + 2] = (byte) ((val >> 8) & 0xff);
    dst[pos + 1] = (byte) ((val >> 16) & 0xff);
    dst[pos + 0] = (byte) ((val >> 24) & 0xff);
  }

  private void longToBytes(byte[] dst, int pos, long val) {
    dst[pos + 7] = (byte) (val & 0xff);
    dst[pos + 6] = (byte) ((val >> 8) & 0xff);
    dst[pos + 5] = (byte) ((val >> 16) & 0xff);
    dst[pos + 4] = (byte) ((val >> 24) & 0xff);

    dst[pos + 3] = (byte) ((val >> 32) & 0xff);
    dst[pos + 2] = (byte) ((val >> 40) & 0xff);
    dst[pos + 1] = (byte) ((val >> 48) & 0xff);
    dst[pos + 0] = (byte) ((val >> 56) & 0xff);
  }

}
