package ch.anjo.chatter.http;

public class WebMessage {
  private final String from;
  private final String content;

  public WebMessage(String from, String content) {
    this.from = from;
    this.content = content;
  }

  public String getFrom() {
    return from;
  }

  public String getContent() {
    return content;
  }
}
