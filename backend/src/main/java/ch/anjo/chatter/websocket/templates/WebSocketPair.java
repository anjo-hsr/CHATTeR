package ch.anjo.chatter.websocket.templates;

import io.javalin.websocket.WsSession;
import java.util.Objects;

public class WebSocketPair {

  private WsSession frontendSession;
  private WsSession backendSession;

  public WebSocketPair() {
    this.frontendSession = null;
    this.backendSession = null;
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

  private String getFrontendSessionId() {
    String id = "undefined";
    if (Objects.nonNull(frontendSession)) {
      id = frontendSession.getId().substring(0, 9);
    }
    return id;
  }

  private String getBackendSessionId() {
    String id = "undefined";
    if (Objects.nonNull(backendSession)) {
      id = backendSession.getId().substring(0, 9);
    }
    return id;
  }

  @Override
  public String toString() {
    return String.format("frontend: %s\tbackend: %s", getFrontendSessionId(), getBackendSessionId());
  }
}
