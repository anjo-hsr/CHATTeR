package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.websocket.templates.WebSocketPair;
import io.javalin.websocket.WsSession;
import java.util.List;
import java.util.Objects;

public class SessionHandler {

  private WebSocketPair sessions;
  private final String username;

  public SessionHandler(String username) {
    this.sessions = new WebSocketPair();
    this.username = username;
  }

  public void replaceSession(WsSession session, String type) {
    switch (type) {
      case "frontend":
        {
          this.sessions.setFrontendSession(session);
          break;
        }
      case "backend":
        {
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
    return sessions.getBackendSession() != null
        && sessions.getBackendSession().isOpen()
        && sessions.getFrontendSession() != null
        && sessions.getFrontendSession().isOpen();
  }

  public WsSession getSessionSibling(WsSession session) {
    return sessions.getSibling(session);
  }

  public String getUsername() {
    return username;
  }

  public void printSession() {
    System.out.println(
        String.format(
            "%s %s at -> %s", DateGenerator.getDate(), this.username, this.sessions.toString()));
  }

  public String getSessionType(WsSession session) {
    String sessionType = String.format("undefined %s...", session.getId().substring(0, 9));
    if (Objects.nonNull(getFrontendSession()) && session.getId().equals(getFrontendSession().getId())) {
      sessionType = "Frontend";
    }
    if (Objects.nonNull(getBackendSession()) && session.getId().equals(getBackendSession().getId())) {
      sessionType = "Backend";
    }
    return sessionType;
  }
}
