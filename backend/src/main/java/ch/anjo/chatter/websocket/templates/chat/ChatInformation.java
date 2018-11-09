package ch.anjo.chatter.websocket.templates.chat;

import java.util.List;

public class ChatInformation {

  public String name;
  public List<String> peers;

  public String getName() {
    return name;
  }

  public List<String> getPeers() {
    return peers;
  }
}
