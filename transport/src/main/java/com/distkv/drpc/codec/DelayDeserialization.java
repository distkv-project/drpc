package com.distkv.drpc.codec;

public class DelayDeserialization {

  private final Serialization serialization;

  private final byte[] data;

  public DelayDeserialization(Serialization serialization, byte[] data) {
    this.serialization = serialization;
    this.data = data;
  }

  public Serialization getSerialization() {
    return serialization;
  }

  public byte[] getData() {
    return data;
  }

}
