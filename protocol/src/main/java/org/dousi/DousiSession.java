package org.dousi;

import org.dousi.utils.StringUtils;

public class DousiSession {

  private byte[] sessionId;

  public static DousiSession createSession(String sessionId) {
    byte[] byteStr = sessionId.getBytes();
    return new DousiSession(byteStr);
  }

  private DousiSession(byte[] sessionId) {
    this.sessionId = sessionId;
  }

  public byte[] getSessionID() {
    return sessionId;
  }

  public static DousiSession fromRandom() {
    String randomStr = StringUtils.getRandomString(16);
    return new DousiSession(randomStr.getBytes());
  }
}
