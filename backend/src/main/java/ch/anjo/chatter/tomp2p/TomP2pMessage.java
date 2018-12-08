package ch.anjo.chatter.tomp2p;

import com.google.common.base.Objects;
import java.io.Serializable;
import net.tomp2p.peers.PeerAddress;

public class TomP2pMessage implements Serializable {
  private final String sender;
  private final String receiver;
  private final PeerAddress receiverAddress;
  private final String jsonMessage;
  private boolean verified;

  public TomP2pMessage(String sender, String receiver, String jsonMessage) {
    this.sender = sender;
    this.receiver = receiver;
    this.receiverAddress = null;
    this.jsonMessage = jsonMessage;
    this.verified = false;
  }

  String getSender() {
    return sender;
  }

  String getJsonMessage() {
    return jsonMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TomP2pMessage that = (TomP2pMessage) o;
    return verified == that.verified
        && Objects.equal(sender, that.sender)
        && Objects.equal(receiver, that.receiver)
        && Objects.equal(receiverAddress, that.receiverAddress)
        && Objects.equal(jsonMessage, that.jsonMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(sender, receiver, receiverAddress, jsonMessage, verified);
  }
}
