package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.websocket.templates.message.MessageInformation;
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

  public void saveMessage(String chatId, MessageInformation message) {
    System.out.println(
        String.format("WebSocketMessage saved in : %s - %s", chatId ,message.messageId)
    );
    chatHandler.saveMessage(chatId, message);
  }

  public void updateMessage(String chatId, String messageId, String signer) {
    System.out.println(
        String.format("User %s signed the message: %s...@%s...",
            signer, messageId.substring(0,9), chatId.substring(0,9))
    );
    chatHandler.updateMessage(chatId, messageId, signer);
  }

  public List<String> getMessages(String chatid) {
    return chatHandler.getChatMessages(chatid);
  }
}
