package org.dousi.session;

import org.apache.commons.lang3.RandomStringUtils;

public class DousiSession {

  private byte[] sessionId;

  public static DousiSession createSession() {
    String randomStr = RandomStringUtils.random(16);
    return new DousiSession(randomStr.getBytes());
  }

  private DousiSession(byte[] sessionId) {
    this.sessionId = sessionId;
  }

  public byte[] getSessionID() {
    return sessionId;
  }

}
