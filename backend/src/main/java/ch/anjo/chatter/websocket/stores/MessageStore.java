package ch.anjo.chatter.websocket.stores;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class MessageStore {

  private List<JsonObject> messages;

  public MessageStore(ArrayList<JsonObject> messages) {
    this.messages = messages;
  }

  public List<JsonObject> getMessages() {
    return messages;
  }

  public void addMessage(JsonObject newMessage) {
    messages.add(newMessage);
  }
}
