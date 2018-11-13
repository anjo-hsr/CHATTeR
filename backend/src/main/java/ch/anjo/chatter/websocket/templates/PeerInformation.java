package ch.anjo.chatter.websocket.templates;

public class PeerInformation {
  public String name;
  public boolean isOnline;

  public PeerInformation(String name) {
    this.name = name;
    this.isOnline = false;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return name;
  }

  public boolean isOnline() {
    return isOnline;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", name, isOnline);
  }
}
