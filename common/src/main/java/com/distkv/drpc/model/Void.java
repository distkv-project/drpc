package com.distkv.drpc.model;


public class Void {

  private Void() {
  }

  public static Void getInstance() {
    return InstanceHolder.aVoid;
  }

  private static class InstanceHolder {
    private static Void aVoid = new Void();
  }

}
