package org.dousi;

public class DousiSession {

  private String sessionID;

  public static DousiSession createDousiSession(String sessionID) {
    return new DousiSession(sessionID);
  }

  private DousiSession(String sessionID) {
    this.sessionID = sessionID;
  }

  public String getSessionID() {
    return sessionID;
  }
}
