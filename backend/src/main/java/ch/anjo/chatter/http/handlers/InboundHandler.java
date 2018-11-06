package ch.anjo.chatter.http.handlers;

import ch.anjo.chatter.http.ConnectionRequest;
import ch.anjo.chatter.http.templates.Message;
import ch.anjo.chatter.lib.HttpPeer;
import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class InboundHandler {

  public static void handleSession(SessionHandler sessionHandler, WsSession session) {
    String username = session.queryParam("username");
    if (username != null) {
      sessionHandler.saveSession(session, username);
      OutboundHandler.broadcastPeers(sessionHandler);
    }
  }

  public static void handleMessageTypes(SessionHandler sessionHandler, Message message) {
    switch (message.type) {
      case "ADD_MESSAGE":
        saveMessage(sessionHandler, message.message);
        break;
      case "SET_USERNAME":
        setUsername(sessionHandler, message);
        break;
      case "SET_CONNECTION":
        System.out.println(message.message);
        break;
    }
  }

  private static void setUsername(SessionHandler sessionHandler, Message message) {
    sessionHandler.setUsername(message.username);
    OutboundHandler.broadcastPeers(sessionHandler);
    sessionHandler.printSession();
  }

  public static void saveMessage(SessionHandler sessionHandler, String message) {
    JsonObject messageObject = createMessageObject(sessionHandler.getUsername(), true, message);
  }

  public static void saveMessage(String sender, String message) {
    createMessageObject(sender, false, message);
  }

  private static JsonObject createMessageObject(String sender, Boolean isMe, String message) {
    JsonObject messageObject = new JsonObject();
    messageObject.addProperty("author", sender);
    messageObject.addProperty("isMe", isMe);
    messageObject.addProperty("message", message);
    messageObject.addProperty("date", new Date().toString());
    System.out.println(messageObject.toString());
    return messageObject;
  }

  private static String getCurrentDate() {
    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(new Date());
  }
}