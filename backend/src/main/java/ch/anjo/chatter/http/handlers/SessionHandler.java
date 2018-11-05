package ch.anjo.chatter.http.handlers;

import io.javalin.websocket.WsSession;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {

  private int sessionCounter = 1;
  private final Map<String, WsSession> sessionNameSessionMap;

  public SessionHandler() {
    this.sessionNameSessionMap = new ConcurrentHashMap<>();
  }

  public void saveSession(String sessionName, WsSession session) {
    sessionName = sessionName.equals("") ? "UserID" + sessionCounter++ : sessionName;
    this.sessionNameSessionMap.put(sessionName, session);
  }

  public Map<String, WsSession> getSessionNameSessionMap() {
    return sessionNameSessionMap;
  }

  public WsSession getSession(String key) {
    return this.sessionNameSessionMap.get(key);
  }

  public Set<String> getKeySet() {
    return this.sessionNameSessionMap.keySet();
  }

  public String getSessionName(WsSession session) {
    Optional<String> possibleUsername =
        this.sessionNameSessionMap
            .keySet()
            .stream()
            .filter(username -> this.sessionNameSessionMap.get(username).equals(session))
            .findFirst();
    return possibleUsername.orElse("");
  }

  public void printSession(WsSession session) {
    sessionNameSessionMap.keySet()
        .stream()
        .filter(sessionName -> sessionNameSessionMap.get(sessionName).equals(session))
        .forEach(sessionName -> System.out.println(sessionName + " at -> " + session.toString()));
  }


  public void printSessions() {
    sessionNameSessionMap.keySet().forEach(sessionName -> System.out
        .println(sessionName + " at -> " + sessionNameSessionMap.get(sessionName).toString())
    );
  }

  public void changeName(String message, WsSession session) {
    String sessionName = this.getSessionName(session);
    System.out.println(sessionName);
    this.sessionNameSessionMap.remove(sessionName);
    this.sessionNameSessionMap.put(message, session);
  }
}
