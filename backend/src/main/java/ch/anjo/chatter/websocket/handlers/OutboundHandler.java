package ch.anjo.chatter.websocket.handlers;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.websocket.WsSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class OutboundHandler {

  OutboundHandler() {}

  public static void sendChats(Handler handler) {
    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
    if (frontendSession == null) {
      return;
    }

    JsonObject message = new JsonObject();
    message.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_CHATS);

    JsonObject chats = new JsonObject();

    HashMap<String, ChatInformation> chatStore = handler.getChatHandler().getChatStore();
    chatStore
        .keySet()
        .forEach(
            chatId -> {
              ChatInformation chatInformation = chatStore.get(chatId);
              JsonObject jsonChatInformation = new JsonObject();
              jsonChatInformation.addProperty("name", chatInformation.getName());

              JsonArray peers = new JsonArray();
              chatInformation.getPeers().forEach(peers::add);
              jsonChatInformation.add("peers", peers.getAsJsonArray());

              chats.add(chatId, jsonChatInformation);
            });
    message.add("chats", chats);

    frontendSession.send(message.toString());
  }

  public static void sendUsername(Handler handler) {
    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
    if (frontendSession == null) {
      return;
    }

    JsonObject message = new JsonObject();
    message.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.SET_USERNAME);
    message.addProperty("username", handler.getSessionHandler().getUsername());

    frontendSession.send(message.toString());
  }

  public static void sendPeers(Handler handler, String jsonMessage) {
    sendMessage(handler.getSessionHandler().getFrontendSession(), jsonMessage);
  }

  private static void sendMessage(WsSession session, String messageString) {
    session.send(messageString);
  }

  public static void sendMessageToSibling(
      Handler handler, WsSession session, String messageString) {
    if (handler.getSessionHandler().existsSessionSibling()) {
      WsSession sessionSibling = handler.getSessionHandler().getSessionSibling(session);
      sessionSibling.send(messageString);
    }
  }

  static void sendChatMessages(Handler handler, String chatId) {
    WsSession session = handler.getSessionHandler().getFrontendSession();
    List<String> messages = handler.getChatHandler().getChatMessages(chatId);
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_MESSAGES);

    JsonArray jsonMessages = new JsonArray();
    JsonParser jsonParser = new JsonParser();
    messages.forEach(
        stringMessage -> {
          JsonObject jsonMessage = jsonParser.parse(stringMessage).getAsJsonObject();
          jsonMessages.add(jsonMessage);
        });

    response.addProperty("chatId", chatId);
    response.add("messages", jsonMessages);

    session.send(response.toString());
  }

  static void sendChatPeers(Handler handler, WsSession session, String id, String chatId) {
    List<String> peerList = handler.getChatHandler().getChatInformation(chatId).getPeers();

    JsonObject peerMessage = new JsonObject();
    peerMessage.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.SEND_CHAT_PEERS);
    peerMessage.addProperty("id", id);
    peerMessage.addProperty("chatId", chatId);

    JsonArray chatPeers = new JsonArray();
    peerList.forEach(
        friend -> {
          JsonObject chatPeer = new JsonObject();
          chatPeer.addProperty("name", friend);
          chatPeers.add(chatPeer);
        });
    peerMessage.add("peers", chatPeers);

    session.send(peerMessage.toString());
  }

  public static String createGetPeer() {
    JsonObject getPeer = new JsonObject();
    getPeer.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_PEERS);
    JsonArray peers = new JsonArray();
    getPeer.add("peers", peers);

    return getPeer.toString();
  }

  private String readResource(final String fileName, Charset charset) throws IOException {
    return Resources.toString(Resources.getResource(fileName), charset);
  }
}
