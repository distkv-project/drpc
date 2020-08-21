package org.drpc.session;

import org.apache.commons.lang3.RandomStringUtils;

public class DrpcSession {

  private byte[] sessionId;

  public static DrpcSession createSession() {
    String randomStr = RandomStringUtils.random(16);
    return new DrpcSession(randomStr.getBytes());
  }

  private DrpcSession(byte[] sessionId) {
    this.sessionId = sessionId;
  }

  public byte[] getSessionID() {
    return sessionId;
  }

}
