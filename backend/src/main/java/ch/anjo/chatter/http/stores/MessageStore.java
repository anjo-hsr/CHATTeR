package ch.anjo.chatter.http.stores;

import com.google.gson.JsonObject;
import java.util.ArrayList;

public class MessageStore {

  private ArrayList<JsonObject> messages;

  public MessageStore(ArrayList<JsonObject> messages) {
    this.messages = messages;
  }

  public ArrayList<JsonObject> getMessages() {
    return messages;
  }

  public void addMessage(JsonObject newMessage) {
    messages.add(newMessage);
  }
}
