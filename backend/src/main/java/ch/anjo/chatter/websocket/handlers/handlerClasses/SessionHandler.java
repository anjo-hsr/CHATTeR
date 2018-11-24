package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.websocket.templates.WebSocketPair;
import io.javalin.websocket.WsSession;
import java.util.List;

public class SessionHandler {

  private WebSocketPair sessions;
  private final String username;

  public SessionHandler(String username) {
    this.sessions = new WebSocketPair();
    this.username = username;
  }

  public void replaceSession(WsSession session, String type) {
    switch (type) {
      case "frontend": {
        this.sessions.setFrontendSession(session);
        break;
      }
      case "backend": {
        this.sessions.setBackendSession(session);
        break;
      }
    }
  }

  public List<WsSession> getSessions() {
    return sessions.getSessions();
  }

  public WsSession getFrontendSession() {
    return sessions.getFrontendSession();
  }

  public WsSession getBackendSession() {
    return sessions.getBackendSession();
  }

  public boolean existsSessionSibling() {
    return sessions.getBackendSession() != null && sessions.getFrontendSession() != null;
  }

  public WsSession getSessionSibling(WsSession session) {
    return sessions.getSibling(session);
  }

  public String getUsername() {
    return username;
  }

  public void printSession() {
    System.out.println(
        DateGenerator.getDate() + this.username + " at -> " + this.sessions.toString());
  }
}
