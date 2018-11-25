package ch.anjo.chatter.websocket.templates.message;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;

public class MessageInformation {
  public String messageId;
  public String date;
  public String author;
  public String message;
  public ArrayList<String> signedBy;


  @Override
  public String toString() {
    JsonObject stringResponse = new JsonObject();
    JsonArray signers = new JsonArray();

    stringResponse.addProperty("messageId", messageId);
    stringResponse.addProperty("date", date);
    stringResponse.addProperty("author", author);
    stringResponse.addProperty("message", message);
    signedBy.forEach(signers::add);
    stringResponse.add("signedBy", signers);

    return stringResponse.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageInformation that = (MessageInformation) o;
    return Objects.equal(messageId, that.messageId)
        && Objects.equal(date, that.date)
        && Objects.equal(author, that.author)
        && Objects.equal(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(messageId, date, author, message);
  }
}
