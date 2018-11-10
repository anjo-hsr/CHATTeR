package ch.anjo.chatter.websocket.templates;

import io.javalin.websocket.WsSession;
import java.util.Arrays;
import java.util.List;

public class WebSocketPair {

  private WsSession frontendSession;
  private WsSession backendSession;

  public WebSocketPair() {
    this.frontendSession = null;
    this.backendSession = null;
  }

  public WebSocketPair(WsSession frontendSession, WsSession backendSession) {
    this.frontendSession = frontendSession;
    this.backendSession = backendSession;
  }

  public List<WsSession> getSessions() {
    return List.of(frontendSession, backendSession);
  }

  public WsSession getFrontendSession() {
    return frontendSession;
  }

  public WsSession getBackendSession() {
    return backendSession;
  }

  public WsSession getSibling(WsSession session) {
    if (frontendSession.getId().equals(session.getId())) {
      return backendSession;
    } else {
      return frontendSession;
    }
  }

  public void setFrontendSession(WsSession frontendSession) {
    this.frontendSession = frontendSession;
  }

  public void setBackendSession(WsSession backendSession) {
    this.backendSession = backendSession;
  }

  @Override
  public String toString() {
    return String.format("frontend: %s\tbackend: %s", frontendSession, backendSession);
  }
}
