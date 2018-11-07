package ch.sbi.blockchat.lib;

import java.util.ArrayList;

public class ChatHistory {
  private ClientPeer friend;
  private ArrayList<Message> messages;
  private String email;

  public ChatHistory() {}

  public ClientPeer getClientPeer() {
    return friend;
  }

  public void setClientPeer(ClientPeer friend) {
    this.friend = friend;
  }

  public ArrayList<Message> getMessages() {
    return messages;
  }

  public void setMessages(ArrayList<Message> messages) {
    this.messages = messages;
  }

  public void addMessage(Message message) {
    this.messages.add(message);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
