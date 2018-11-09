package ch.anjo.chatter.http.handlers;

import ch.anjo.chatter.http.handlers.handlerClasses.Handler;
import ch.anjo.chatter.http.templates.Message;
import ch.anjo.chatter.http.templates.message.MessageInformation;
import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;

public class InboundHandler {

  public static void handleSession(Handler handler, WsSession session) {
    String username = session.queryParam("username");
    String wsType = session.queryParam("wsType");
    if (wsType != null) {
      handler.getSessionHandler().replaceSession(session, wsType);
      handler.getSessionHandler().setUsername(username);
    }
  }

  public static void handleMessageTypes(Handler handler, WsSession session, String jsonMessage, Message message) {
    switch (message.type) {
      case "ADD_MESSAGE":
        saveMessage(handler, message.chatId, message.messageInformation);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      case "SET_USERNAME":
        setUsername(handler, message);
        break;
      case "ADD_CHAT":
      case "CHANGE_CHAT":
        handler.getChatHandler().saveChat(message);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      case "DELETE_CHAT":
        handler.getChatHandler().deleteChat(message.chatId);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      case "SELECT_CHAT":
        OutboundHandler.sendChatMessages(handler, message.chatId);
    }
  }

  private static void setUsername(Handler handler, Message message) {
    handler.setUsername(message.username);
    OutboundHandler.sendPeers(handler);
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
