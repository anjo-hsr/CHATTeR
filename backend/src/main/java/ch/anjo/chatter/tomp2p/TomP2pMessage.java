package ch.anjo.chatter.tomp2p;

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

  public TomP2pMessage(String sender, PeerAddress receiverAddress, String jsonMessage) {
    this.sender = sender;
    this.receiver = "";
    this.receiverAddress = receiverAddress;
    this.jsonMessage = jsonMessage;
    this.verified = false;
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public PeerAddress getReceiverAddress() {
    return receiverAddress;
  }

  public String getJsonMessage() {
    return jsonMessage;
  }

  public boolean isVerified() {
    return verified;
  }

  public void verify() {
    verified = true;
  }
}
