package ch.anjo.chatter.websocket;

import java.io.Serializable;
import java.util.Date;

public class WebMessage implements Serializable {

  private final String from;
  private final String content;
  private final Date date;

  public WebMessage(String from, String content) {
    this.from = from;
    this.content = content;
    date = new Date();
  }

  public String getFrom() {
    return from;
  }

  public String getContent() {
    return content;
  }

  public Date getDate() {
    return date;
  }
}
