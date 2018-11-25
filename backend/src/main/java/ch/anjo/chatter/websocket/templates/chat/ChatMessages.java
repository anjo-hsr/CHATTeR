package ch.anjo.chatter.websocket.templates.chat;

import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatMessages {
  private List<MessageInformation> messages;

  public ChatMessages() {
    this.messages = new ArrayList<>();
  }

  public List<String> getMessagesAsString() {
    return messages.stream().map(MessageInformation::toString).collect(Collectors.toList());
  }

  public void addMessage(MessageInformation message) {
    messages.add(message);
  }

  public void updateMessage(String messageId, String signer){
    messages.stream()
        .filter(message -> message.messageId.equals(messageId))
        .forEach(message -> message.signedBy.add(signer));
  }
}
