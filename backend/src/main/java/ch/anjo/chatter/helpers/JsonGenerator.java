package ch.anjo.chatter.helpers;

import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterUser;
import ch.anjo.chatter.tomp2p.ChatterWebSocketClient;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.PeerInformation;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class JsonGenerator {

  public static String generateAddChats(Handler handler) {
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
    return message.toString();
  }

  public static String generateAddMessages(String chatId, List<String> messages) {
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
    return response.toString();
  }

  public static String generateAddPeers(ChatterUser friend) {
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();
    peers.add(friend.getInformation());
    response.add("peers", peers);
    return response.toString();
  }

  public static void generateAndSendCheckSender(WebSocketMessage webSocketMessage, ChatterPeer myself,
      ChatterWebSocketClient chatterWebSocketClient) {
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.RESPONSE_CHECK_SIGNATURE);
    response.addProperty("sender", webSocketMessage.senderAddress);
    response.addProperty("chatId", webSocketMessage.chatId);
    response.addProperty("messageId", webSocketMessage.messageId);

    CompletableFuture<String> senderAddress = myself.getNotaryService()
        .checkMessage(webSocketMessage.messageId);
    senderAddress.thenAccept(sender -> {
      response.addProperty("isSenderCorrect", sender.equals(webSocketMessage.senderAddress));
      chatterWebSocketClient.send(response.toString());
    });
  }

  public static String generateConfirmMessage(ChatterPeer chatterPeer, WebSocketMessage webSocketMessage) {
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.CONFIRM_MESSAGE);
    response.addProperty("username", chatterPeer.getChatterUser().getUsername());
    response.addProperty("chatId", webSocketMessage.chatId);
    response.addProperty("messageId", webSocketMessage.messageInformation.messageId);
    return response.toString();
  }

  public static String generateGetChatPeers(WebSocketMessage webSocketMessage, String messageId) {
    JsonObject getChatPeers = new JsonObject();
    getChatPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_CHAT_PEERS);
    getChatPeers.addProperty("chatId", webSocketMessage.chatId);
    getChatPeers.addProperty("id", messageId);
    return getChatPeers.toString();
  }

  public static String generateGetPeers() {
    JsonObject getPeer = new JsonObject();
    getPeer.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_PEERS);
    JsonArray peers = new JsonArray();
    getPeer.add("peers", peers);

    return getPeer.toString();
  }

  public static String generateSendChatPeers(String id, String chatId, List<String> peerList) {
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
    return peerMessage.toString();
  }

  public static String generateSetUsername(Handler handler) {
    JsonObject message = new JsonObject();
    message.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.SET_USERNAME);
    message.addProperty("username", handler.getSessionHandler().getUsername());
    return message.toString();
  }

  public static String generateUpdateChatPeers(ChatterPeer myself, Set<PeerInformation> peers) {
    JsonObject getChatPeers = new JsonObject();
    getChatPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.UPDATE_CHAT_PEERS);
    JsonArray chatPeers = new JsonArray();

    peers
        .stream()
        .filter(peer -> !myself.getChatterUser().getFriends().contains(peer.getName()))
        .forEach(peer -> myself.addFriend(peer.name));

    peers.forEach(
        peer -> {
          JsonObject chatPeer = new JsonObject();
          chatPeer.addProperty("name", peer.name);
          chatPeers.add(chatPeer);
        });

    getChatPeers.add("peers", chatPeers);
    return getChatPeers.toString();
  }
}
