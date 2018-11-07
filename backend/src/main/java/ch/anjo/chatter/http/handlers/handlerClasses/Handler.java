package ch.anjo.chatter.http.handlers.handlerClasses;

import io.javalin.websocket.WsSession;

public class Handler {

  private ChatHandler chatHandler;
  private SessionHandler sessionHandler;

  public Handler(WsSession session, String username) {
    this.chatHandler = new ChatHandler();
    this.sessionHandler = new SessionHandler(session, username);
  }

  public ChatHandler getChatHandler() {
    return chatHandler;
  }

  public SessionHandler getSessionHandler() {
    return sessionHandler;
  }

  public void saveSession(WsSession session, String username) {
    sessionHandler.saveSession(session, username);
  }

  public void setUsername(String username) {
    sessionHandler.setUsername(username);
  }

  public void saveMessage(String chatId, String message) {
    System.out.println("Message saved in :" + chatId + " - " + message);
    chatHandler.saveMessage(chatId, message);
  }
}
