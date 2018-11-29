package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.tomp2p.helpers.LoopHandler;
import ch.anjo.chatter.websocket.templates.PeerInformation;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

public class ChatterWebSocketClient extends WebSocketClient {

  private final int webSocketPort;
  private final ChatterPeer myself;
  private final String username;
  private Map<String, Set<PeerInformation>> chatMemebersMap;
  private final Map<String, String> messageWaitingRoom;
  private LoopHandler loopHandler;

  public ChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself)
      throws URISyntaxException {
    super(new URI("ws://localhost:" + webSocketPort + "/chat?wsType=backend"));
    this.webSocketPort = webSocketPort;
    this.myself = myself;
    this.username = username;
    this.chatMemebersMap = new HashMap<>();
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
    System.out.println(DateGenerator.getDate() + "Closed connection:\n" + reason);
    ChatterWebSocketClient.generateChatterWebSocketClient(this.webSocketPort, this.username, this.myself);
  }

  @Override
  public void onError(Exception ex) {
    System.err.println("Error occurred");
    ex.printStackTrace();
    ChatterWebSocketClient.generateChatterWebSocketClient(this.webSocketPort, this.username, this.myself);
  }

  public static void generateChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself){
    try {
      ChatterWebSocketClient webSocketClient = new ChatterWebSocketClient(webSocketPort, username, myself);
      if (!myself.getMasterName().equals(myself.getChatterUser().getUsername())) {
        myself.addFriend(myself.getMasterName());
      }

      ChannelAction.replyToTomP2PData(myself, webSocketClient);
      webSocketClient.run();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private void handleWebSocketMessages(String jsonMessage) {
    Gson gson = new Gson();
    WebSocketMessage webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);

    switch (webSocketMessage.type) {
      case MessageTypes.ADD_MESSAGE:
        {
          sendMessageToPeers(webSocketMessage, jsonMessage);
          break;
        }
      case MessageTypes.SET_USERNAME:
        {
          break;
        }
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT:
        {
          Set<PeerInformation> peers =
              webSocketMessage
                  .chatInformation
                  .peers
                  .stream()
                  .map(PeerInformation::new)
                  .filter(peer -> !myself.getChatterUser().getUsername().equals(peer.name))
                  .collect(Collectors.toSet());
          chatMemebersMap.put(webSocketMessage.chatId, Sets.newHashSet(peers));
          bootStrapNewPeers(peers);
          sendNewChat(webSocketMessage, jsonMessage);
          break;
        }
      case MessageTypes.SELECT_CHAT:
        {
          break;
        }
      case MessageTypes.SEND_CHAT_PEERS:
        {
          updateChatPeerMap(webSocketMessage);
          break;
        }
      case MessageTypes.GET_PEERS:
        {
          sendPeers(webSocketMessage);
          break;
        }
      case MessageTypes.APPROVE_CHAT:
        {
          bootStrapNewPeers(webSocketMessage.chatId);
          break;
        }
      default:
    }
  }

  private void sendPeers(WebSocketMessage webSocketMessage) {
    Set<String> friends = myself.getChatterUser().getFriends();
    friends
        .stream()
        .map(
            friend ->
                myself
                    .getDht()
                    .get(ChatterUser.getHash(friend))
                    .start()
                    .awaitUninterruptibly()
                    .data())
        .filter(Objects::nonNull)
        .map(ChatterPeer::readUser)
        .filter(Objects::nonNull)
        .forEach(
            friend -> {
              send(generateAddPeer(friend));
            });
  }

  @NotNull
  private String generateAddPeer(ChatterUser friend) {
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();

    JsonObject peer = new JsonObject();
    peer.addProperty("name", friend.getUsername());
    peer.addProperty("isOnline", friend.isOnline());
    peers.add(peer);
    response.add("peers", peers);
    return response.toString();
  }

  private void sendNewChat(WebSocketMessage webSocketMessage, String jsonMessage) {
    sendMessageToPeers(webSocketMessage, jsonMessage);
  }

  private void sendMessageToPeers(WebSocketMessage webSocketMessage, String jsonMessage) {
    if (loopHandler.isLoopMessage(webSocketMessage)) {
      return;
    }

    String chatId = webSocketMessage.chatId;
    if (chatMemebersMap.containsKey(chatId)) {
      System.out.println("Direct");
      System.out.println(
          String.format(
              "Send message over chat (%s) to: %s",
              chatId.substring(0, 9), chatMemebersMap.get(chatId).toString()));
      Set<PeerInformation> chatPeers = chatMemebersMap.get(chatId);
      chatPeers.forEach(
          peer -> DataSender.sendWithConfirmation(myself, peer.getUsername(), jsonMessage));
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
    PeerInformation[] peers = webSocketMessage.peers;
    PeerInformation[] otherPeers =
        Arrays.stream(peers)
            .filter(peer -> !peer.getUsername().equals(username))
            .toArray(PeerInformation[]::new);
    HashSet<PeerInformation> peerSet = new HashSet<>(Set.of(otherPeers));

    chatMemebersMap.put(webSocketMessage.chatId, peerSet);

    if (webSocketMessage.id.equals("")) {
      System.out.println("Updated peers via Chat update");
    } else {
      System.out.println("Received peers via new WebSocketMessage.");
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

  private void bootStrapNewPeers(String chatId) {
    Set<PeerInformation> peers = new HashSet<>(chatMemebersMap.get(chatId));
    bootStrapNewPeers(peers);
  }

  private void bootStrapNewPeers(Set<PeerInformation> peers) {
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
    this.send(getChatPeers.toString());
  }
}
