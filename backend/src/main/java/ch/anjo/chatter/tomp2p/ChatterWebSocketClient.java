package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.handlers.handlerClasses.LoopHandler;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ChatterWebSocketClient extends WebSocketClient {

  private final ChatterPeer myself;
  private final String username;
  private Map<String, Set<String>> chatMembers;
  private final Map<String, String> messageWaitingRoom;
  private LoopHandler loopHandler;

  public ChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself) throws URISyntaxException {
    super(new URI("ws://localhost:" + webSocketPort + "/chat?wsType=backend&username=" + username));

    this.myself = myself;
    this.username = username;
    this.chatMembers = new HashMap<>();
    this.messageWaitingRoom = new HashMap<>();
    this.loopHandler = new LoopHandler();
  }

  @Override
  public void onOpen(ServerHandshake handshake) {
    System.out.println(DateGenerator.getDate() + "Connected with WebSocket.");
  }

  @Override
  public void onMessage(String message) {
    handleWebSocketMessages(message);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println(new Date() + "Closed connection:\n" + reason);
  }

  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();
  }


  private void handleWebSocketMessages(String jsonMessage) {
    Gson gson = new Gson();
    WebSocketMessage webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);

    switch (webSocketMessage.type) {
      case MessageTypes.ADD_MESSAGE:
        sendMessageToPeers(webSocketMessage, jsonMessage.replace("\"isMe\":true","\"isMe\":false"));
        break;
      case MessageTypes.SET_USERNAME:
        break;
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT:
        bootStrapNewPeers(webSocketMessage.chatInformation.peers);
        sendNewChat(webSocketMessage, jsonMessage);
        break;
      case MessageTypes.DELETE_CHAT:
        break;
      case MessageTypes.SELECT_CHAT:
        break;
      case MessageTypes.SEND_CHAT_PEERS:
        updateChatPeerMap(webSocketMessage);
        break;
      default:
        return;
    }
  }

  private void sendNewChat(WebSocketMessage webSocketMessage, String jsonMessage) {
    sendMessageToPeers(webSocketMessage, jsonMessage);
  }

  private void sendMessageToPeers(WebSocketMessage webSocketMessage, String jsonMessage) {
    if (loopHandler.isLoopMessage(webSocketMessage)) {
      return;
    }

    String chatId = webSocketMessage.chatId;
    if (chatMembers.containsKey(chatId)) {
      System.out.println(String.format("Send message over chat (%s) to: %s", chatId.substring(0, 9), chatMembers.get(chatId)));
      Set<String> chatPeers = chatMembers.get(chatId);
      System.out.println("Direct");
      chatPeers.forEach(peer -> DataSender.sendWithConfirmation(myself, peer, jsonMessage));
      loopHandler.setNewMessage(webSocketMessage);
      return;
    }
    String messageId = generateMessageId(webSocketMessage);
    messageWaitingRoom.put(messageId, jsonMessage);

    requestPeers(webSocketMessage, messageId);
  }

  private void requestPeers(WebSocketMessage webSocketMessage, String messageId) {
    JsonObject getChatPeers = new JsonObject();
    getChatPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_CHAT_PEERS);
    getChatPeers.addProperty("chatId", webSocketMessage.chatId);
    getChatPeers.addProperty("id", messageId);
    this.send(getChatPeers.toString());
  }

  private void updateChatPeerMap(WebSocketMessage webSocketMessage) {
    String[] peers = webSocketMessage.peers;
    String[] otherPeers = Arrays.stream(peers).filter(peer -> !peer.equals(username)).toArray(String[]::new);
    HashSet<String> peerSet = new HashSet<>(Set.of(otherPeers));

    chatMembers.put(webSocketMessage.chatId, peerSet);

    if (webSocketMessage.id.equals("")) {
      System.out.println("Updated peers via Chat update");
    } else {
      System.out.println("Recieved peers via new WebSocketMessage.");
      sendWaitingMessageToPeers(webSocketMessage);
    }
  }

  private void sendWaitingMessageToPeers(WebSocketMessage webSocketMessage) {
    String jsonMessage = messageWaitingRoom.remove(webSocketMessage.id);
    Gson gson = new Gson();
    webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);
    sendMessageToPeers(webSocketMessage, jsonMessage);
  }

  private String generateMessageId(WebSocketMessage webSocketMessage) {
    byte[] array = new byte[256]; // length is bounded by 7
    new Random().nextBytes(array);
    return Hashing.sha256().hashBytes(array).toString();
  }

  private void bootStrapNewPeers(List<String> peers) {
    peers.stream().filter(peer -> {
      Set<String> friends = myself.getChatterUser().getFriends();
      return !friends.contains(peer);
    })
        .forEach(myself::addFriend);

    JsonObject getChatPeers = new JsonObject();
    getChatPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.UPDATE_CHAT_PEERS);
    JsonArray chatPeers = new JsonArray();
    myself.getChatterUser().getFriends().forEach(chatPeers::add);
    getChatPeers.add("peers", chatPeers);
    this.send(getChatPeers.toString());
  }

}