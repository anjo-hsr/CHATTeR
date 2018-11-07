package ch.anjo.chatter.http.handlers;

import ch.anjo.chatter.http.handlers.handlerClasses.Handler;
import ch.anjo.chatter.http.templates.Message;
import ch.anjo.chatter.http.templates.message.MessageInformation;
import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;

public class InboundHandler {

  public static void handleSession(Handler handler, WsSession session) {
    String username = session.queryParam("username");
    if (username != null) {
      handler.saveSession(session, username);
      OutboundHandler.broadcastPeers(handler.getSessionHandler());
    }
  }

  public static void handleMessageTypes(Handler handler, Message message) {
    switch (message.type) {
      case "ADD_MESSAGE":
        saveMessage(handler, message.chatId, message.messageInformation);
        break;
      case "SET_USERNAME":
        setUsername(handler, message);
        break;
      case "SET_CONNECTION":
        message.connection.printInformation();
        OutboundHandler.sendChats(handler);
        break;
      case "ADD_CHAT":
      case "CHANGE_CHAT":
        handler.getChatHandler().saveChat(message);
        break;
      case "DELETE_CHAT":
        handler.getChatHandler().deleteChat(message.chatId);
        break;
      case "SELECT_CHAT":
        OutboundHandler.sendChatMessages(handler, message.chatId);
    }
  }

  private static void setUsername(Handler handler, Message message) {
    handler.setUsername(message.username);
    OutboundHandler.broadcastPeers(handler.getSessionHandler());
    handler.getSessionHandler().printSession();
  }

  public static void saveMessage(Handler handler, String chatId, MessageInformation message) {
    JsonObject messageInformation = createMessageInformation(message);
    handler.saveMessage(chatId, messageInformation.toString());
  }

  private static JsonObject createMessageInformation(MessageInformation message) {
    JsonObject messageInformation = new JsonObject();
    messageInformation.addProperty("date", message.date);
    messageInformation.addProperty("author", message.author);
    messageInformation.addProperty("message", message.message);
    messageInformation.addProperty("isMe", message.isMe);
    return messageInformation;
  }
}
