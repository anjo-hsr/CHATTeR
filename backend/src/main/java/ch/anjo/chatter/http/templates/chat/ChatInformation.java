package ch.anjo.chatter.http.templates.chat;

import java.util.ArrayList;

public class ChatInformation {

  public String name;
  public ArrayList<String> peers;

  public String getName() {
    return name;
  }

  public ArrayList<String> getPeers() {
    return peers;
  }
}
