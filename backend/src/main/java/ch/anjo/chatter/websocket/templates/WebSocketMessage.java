package ch.anjo.chatter.websocket.templates;

import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;

public class WebSocketMessage {
  public String type;
  public String id;
  public String message;
  public String username;
  public String chatId;
  public String[] peers;
  public MessageInformation messageInformation;
  public ChatInformation chatInformation;
  public boolean confirmed;
}
