package ch.anjo.chatter.lib;

import java.io.Serializable;

public class PeerMessage implements Serializable {
  private final String from;
  private final String to;
  private final String content;

  public PeerMessage(String from, String to, String content) {
    this.from = from;
    this.to = to;
    this.content = content;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public String getContent() {
    return content;
  }
}
