package ch.anjo.chatter.websocket.templates.chat;

import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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

  public boolean updateMessage(String messageId, String signer) {
    AtomicBoolean didMessageExist = new AtomicBoolean(false);
    messages
        .stream()
        .filter(message -> message.messageId.equals(messageId))
        .forEach(message -> {
          didMessageExist.set(true);
          if (!message.signedBy.contains(signer)) {
            message.signedBy.add(signer);
          }
        });
    return didMessageExist.get();
  }

  public boolean contains(String messageId) {
    return messages.stream().anyMatch(message -> message.messageId.equals(messageId));
  }
}
