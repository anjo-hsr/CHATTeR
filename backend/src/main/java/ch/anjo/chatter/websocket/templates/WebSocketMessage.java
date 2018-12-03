package ch.anjo.chatter.websocket.templates;

import ch.anjo.chatchain.Address;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import com.google.common.base.Objects;

public class WebSocketMessage {
  public String type;
  public String id;
  public String message;
  public String username;
  public String chatId;
  public String messageId;
  public PeerInformation[] peers;
  public MessageInformation messageInformation;
  public ChatInformation chatInformation;
  public Address senderAddress;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WebSocketMessage that = (WebSocketMessage) o;
    return Objects.equal(type, that.type)
        && Objects.equal(id, that.id)
        && Objects.equal(message, that.message)
        && Objects.equal(username, that.username)
        && Objects.equal(chatId, that.chatId)
        && Objects.equal(peers, that.peers)
        && Objects.equal(messageInformation, that.messageInformation)
        && Objects.equal(chatInformation, that.chatInformation)
        && Objects.equal(senderAddress, that.senderAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        type, id, message, username, chatId, peers, messageInformation, chatInformation);
  }
}
