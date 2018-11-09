package ch.anjo.chatter.websocket.templates.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatMessages {
  private List<String> messages;

  public ChatMessages() {
    this.messages = new ArrayList<>();
  }

  public ChatMessages(String message) {
    this.messages = new ArrayList<>();
    this.messages.add(message);
  }

  public ChatMessages(ArrayList<String> messages) {
    this.messages = messages;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void addMessage(String message) {
    messages.add(message);
  }
}
