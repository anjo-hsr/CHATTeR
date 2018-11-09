package ch.anjo.chatter.http.templates;

import ch.anjo.chatter.http.templates.chat.ChatInformation;
import ch.anjo.chatter.http.templates.message.MessageInformation;

public class Message {
  public String type;
  public String message;
  public String username;
  public String chatId;
  public MessageInformation messageInformation;
  public ChatInformation chatInformation;
}
