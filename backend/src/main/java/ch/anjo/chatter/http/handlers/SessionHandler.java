package ch.anjo.chatter.http.handlers;

import io.javalin.websocket.WsSession;


public class SessionHandler {

  private WsSession session;
  private String username;

  public SessionHandler(WsSession session, String username) {
    this.session = session;
    this.username = username;
  }

  public WsSession getSession() {
    return this.session;
  }

  public String getUsername() {
    return this.username;
  }

  public void setSession(WsSession session) {
    this.session = session;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void saveSession(WsSession session, String username) {
    this.session = session;
    this.username = username;
  }

  public void printSession() {
    System.out.println(this.username + " at -> " + this.session.toString());
  }
}
