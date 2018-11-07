package ch.anjo.chatter.http.templates.chat;

import java.util.ArrayList;

public class ChatMessages {
  private ArrayList<String> messages;

  public ChatMessages(){
    this.messages = new ArrayList<>();
  }

  public ChatMessages(String message){
    this.messages = new ArrayList<>();
    this.messages.add(message);
  }

  public ChatMessages(ArrayList<String> messages) {
    this.messages = messages;
  }

  public ArrayList<String> getMessages() {
    return messages;
  }

  public void addMessage(String message) {
    messages.add(message);
  }
}
