package com.distkv.drpc.benchmark.dubbo;

import java.io.Serializable;
import lombok.Builder;

public class StringProtocol {

  public enum Status implements Serializable {
    OK,
    ERROR,
    ;
  }

  @Builder
  public static class PutRequest implements Serializable {
    private static final long serialVersionUID = 5027830810124339452L;
    private String key;
    private String value;

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }

  @Builder
  public static class PutResponse implements Serializable {
    private static final long serialVersionUID = 1532129717519993293L;
    private Status status;

    public Status getStatus() {
      return status;
    }
  }

  @Builder
  public static class GetRequest implements Serializable {
    private static final long serialVersionUID = -2040838701878900204L;
    private String key;

    public String getKey() {
      return key;
    }
  }

  @Builder
  public static class GetResponse implements Serializable {
    private static final long serialVersionUID = -4892659417113343338L;
    private Status status;
    private String value;

    public Status getStatus() {
      return status;
    }

    public String getValue() {
      return value;
    }
  }

}
