package ch.anjo.chatter.websocket.templates.message;

import com.google.common.base.Objects;

public class MessageInformation {
  public String messageId;
  public String date;
  public String author;
  public String message;
  public boolean isMe;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageInformation that = (MessageInformation) o;
    return isMe == that.isMe
        && Objects.equal(messageId, that.messageId)
        && Objects.equal(date, that.date)
        && Objects.equal(author, that.author)
        && Objects.equal(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(messageId, date, author, message, isMe);
  }
}
