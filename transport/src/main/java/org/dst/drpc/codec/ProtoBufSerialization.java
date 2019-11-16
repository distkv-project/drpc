package org.dst.drpc.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.protobuf.GeneratedMessage.Builder;
import com.google.protobuf.Message;
import java.io.IOException;
import java.lang.reflect.Method;

public class ProtoBufSerialization implements Serialization {

  @Override
  public byte[] serialize(Object object) throws IOException {
    byte[] result;
    if (object instanceof Builder) {
      object = ((Builder) object).build();
    }
    if (object instanceof Message) {
      result = ((Message) object).toByteArray();
    } else {
      result = backupSerialize(object);
    }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
    Builder builder;
    try {
      Method method = clazz.getMethod("newBuilder");
      builder = (Builder) method.invoke(null, null);
    } catch (Exception e) {
      return backupDeserialize(bytes, clazz);
    }
    builder.mergeFrom(bytes);
    return (T) builder.build();
  }

  private byte[] backupSerialize(Object object) throws IOException {
    SerializeWriter out = new SerializeWriter();
    JSONSerializer serializer = new JSONSerializer(out);
    serializer.config(SerializerFeature.WriteEnumUsingToString, true);
    serializer.config(SerializerFeature.WriteClassName, true);
    serializer.write(object);
    return out.toBytes("UTF-8");
  }

  private <T> T backupDeserialize(byte[] bytes, Class<T> clazz) throws IOException {
    return JSON.parseObject(new String(bytes), clazz);
  }
}
