package ch.anjo.chatter.lib;

public class ChatterMessage {
  private final String sender;
  private final String receiver;
  private final String message;
  private boolean verified;

  public ChatterMessage(String sender, String receiver, String message) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
    this.verified = false;
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public String getMessage() {
    return message;
  }

  public boolean isVerified() {
    return verified;
  }

  public void verify() {
    verified = true;
  }
}
