package ch.anjo.chatter.websocket.handlers.handlerClasses;

import io.javalin.websocket.WsSession;
import java.util.List;

public class Handler {

  private ChatHandler chatHandler;
  private SessionHandler sessionHandler;

  public Handler(WsSession session, String username) {
    this.chatHandler = new ChatHandler();
    this.sessionHandler = new SessionHandler(username);
  }

  public ChatHandler getChatHandler() {
    return chatHandler;
  }

  public SessionHandler getSessionHandler() {
    return sessionHandler;
  }

  public void setUsername(String username) {
    sessionHandler.setUsername(username);
  }

  public void saveMessage(String chatId, String message) {
    System.out.println("WebSocketMessage saved in :" + chatId + " - " + message);
    chatHandler.saveMessage(chatId, message);
  }

  public List<String> getMessages(String chatid) {
    return chatHandler.getChatMessages(chatid);
  }
}
