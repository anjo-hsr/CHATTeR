package ch.anjo.chatter.websocket.templates;

import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;

public class Message {
  public String type;
  public String message;
  public String username;
  public String chatId;
  public MessageInformation messageInformation;
  public ChatInformation chatInformation;
}
