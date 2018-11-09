package ch.anjo.chatter.websocket.handlers;

import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.Peer;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.websocket.WsSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OutboundHandler {

  public OutboundHandler() {
  }

  public static void sendChats(Handler handler) {
    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
    if (frontendSession == null) {
      return;
    }

    JsonObject message = new JsonObject();
    message.addProperty("type", "ADD_CHATS");

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
    message.addProperty("type", "SET_USERNAME");
    message.addProperty("username", handler.getSessionHandler().getUsername());

    frontendSession.send(message.toString());
  }

  public static void sendPeers(Handler handler) {
    Peer[] peers = getPeerArray();
    JsonArray jsonPeers = new JsonArray();

    Arrays.stream(peers)
        .forEach(
            peer -> {
              jsonPeers.add(peer.username);
            });

    JsonObject message = new JsonObject();
    message.addProperty("type", "ADD_PEERS");
    message.addProperty("peers", jsonPeers.toString());

    sendMessage(handler.getSessionHandler().getFrontendSession(), message.toString());
  }

  private static Peer[] getPeerArray() {
    OutboundHandler outboundHandler = new OutboundHandler();
    String jsonString = null;
    try {
      jsonString = outboundHandler.readResource("peers/peers.json", Charsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Gson gson = new Gson();
    return gson.fromJson(jsonString, Peer[].class);
  }

  private static void sendMessage(WsSession session, String messageString) {
    session.send(messageString);
  }

  public static void sendMessageToSibling(Handler handler, WsSession session, String messageString){
    if(handler.getSessionHandler().existsSessionSibling()){
      WsSession sessionSibling = handler.getSessionHandler().getSessionSibling(session);
      sessionSibling.send(messageString);
    }
  }

  public static void sendChatMessages(Handler handler, String chatId) {
    WsSession session = handler.getSessionHandler().getFrontendSession();
    List<String> messages = handler.getChatHandler().getChatMessages(chatId);
    JsonObject response = new JsonObject();
    response.addProperty("type", "ADD_MESSAGES");

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

  public String readResource(final String fileName, Charset charset) throws IOException {
    return Resources.toString(Resources.getResource(fileName), charset);
  }
}
