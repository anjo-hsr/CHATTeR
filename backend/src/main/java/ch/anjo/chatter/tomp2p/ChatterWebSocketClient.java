package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.helpers.JsonGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataReceiver;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.tomp2p.helpers.LoopHandler;
import ch.anjo.chatter.websocket.templates.PeerInformation;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ChatterWebSocketClient extends WebSocketClient {

  private final int webSocketPort;
  private final ChatterPeer myself;
  private final String username;
  private Map<String, Set<PeerInformation>> chatMembersMap;
  private final Map<String, String> messageWaitingRoom;
  private LoopHandler loopHandler;

  private ChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself)
      throws URISyntaxException {
    super(new URI("ws://localhost:" + webSocketPort + "/chat?wsType=backend"));
    this.webSocketPort = webSocketPort;
    this.myself = myself;
    this.username = username;
    this.chatMembersMap = new HashMap<>();
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

  public static void generateChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself) {
    try {
      ChatterWebSocketClient webSocketClient = new ChatterWebSocketClient(webSocketPort, username, myself);
      if (!myself.getMasterName().equals(myself.getChatterUser().getUsername())) {
        myself.addFriend(myself.getMasterName());
      }

      DataReceiver.replyToTomP2PData(myself, webSocketClient);
      webSocketClient.run();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private void handleWebSocketMessages(String jsonMessage) {
    Gson gson = new Gson();
    WebSocketMessage webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);

    switch (webSocketMessage.type) {
      case MessageTypes.ADD_MESSAGE: {
        sendMessageToPeers(webSocketMessage, jsonMessage);
        break;
      }
      case MessageTypes.CHECK_SIGNATURE: {
        checkSender(webSocketMessage);
        break;
      }
      case MessageTypes.SET_USERNAME: {
        break;
      }
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT: {
        List<String> searchPeers = webSocketMessage.chatInformation.peers;
        if (Objects.nonNull(webSocketMessage.chatInformation.oldPeers)) {
          searchPeers = webSocketMessage.chatInformation.oldPeers;
        }
        Set<PeerInformation> peers =
            searchPeers
                .stream()
                .map(PeerInformation::new)
                .filter(peer -> !myself.getChatterUser().getUsername().equals(peer.name))
                .collect(Collectors.toSet());
        chatMembersMap.put(webSocketMessage.chatId, Sets.newHashSet(peers));
        this.send(JsonGenerator.generateUpdateChatPeers(myself, peers));
        sendNewChat(webSocketMessage, jsonMessage);
        break;
      }
      case MessageTypes.SELECT_CHAT: {
        this.send(ChannelAction.getFriends(myself));
        break;
      }
      case MessageTypes.SEND_CHAT_PEERS: {
        updateChatPeerMap(webSocketMessage);
        break;
      }
      case MessageTypes.GET_PEERS: {
        sendPeers();
        break;
      }
      default:
    }
  }

  private void checkSender(WebSocketMessage webSocketMessage) {
    JsonGenerator.generateAndSendCheckSender(webSocketMessage, myself, this);
  }

  private void sendPeers() {
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
              send(JsonGenerator.generateAddPeers(friend));
            });
  }

  private void sendNewChat(WebSocketMessage webSocketMessage, String jsonMessage) {
    sendMessageToPeers(webSocketMessage, jsonMessage);
  }

  private void sendMessageToPeers(WebSocketMessage webSocketMessage, String jsonMessage) {
    if (loopHandler.isLoopMessage(webSocketMessage)) {
      return;
    }

    String chatId = webSocketMessage.chatId;
    if (chatMembersMap.containsKey(chatId)) {
      System.out.println("Direct");
      System.out.println(
          String.format(
              "Send message over chat (%s) to: %s",
              chatId.substring(0, 9), chatMembersMap.get(chatId).toString()));

      Set<PeerInformation> chatPeers = chatMembersMap.get(chatId);
      if (webSocketMessage.type.equals(MessageTypes.ADD_MESSAGE)) {
        this.myself.getNotaryService().storeMessage(webSocketMessage.messageInformation.messageId);
      }
      chatPeers.forEach(
          peer -> DataSender.sendWithConfirmation(myself, peer.getUsername(), jsonMessage));
      loopHandler.setNewMessage(webSocketMessage);
      return;
    }
    String waitingMessageId = generateWaitingMessageId();
    messageWaitingRoom.put(waitingMessageId, jsonMessage);

    requestPeers(webSocketMessage, waitingMessageId);
  }

  private void requestPeers(WebSocketMessage webSocketMessage, String waitingMessageId) {
    String getChatPeers = JsonGenerator.generateGetChatPeers(webSocketMessage, waitingMessageId);
    this.send(getChatPeers);
  }

  private void updateChatPeerMap(WebSocketMessage webSocketMessage) {
    PeerInformation[] peers = webSocketMessage.peers;
    PeerInformation[] otherPeers =
        Arrays.stream(peers)
            .filter(peer -> !peer.getUsername().equals(username))
            .toArray(PeerInformation[]::new);
    HashSet<PeerInformation> peerSet = new HashSet<>(Set.of(otherPeers));

    chatMembersMap.put(webSocketMessage.chatId, peerSet);

    if (webSocketMessage.waitingMessageId.equals("")) {
      System.out.println("Updated peers via Chat update");
    } else {
      System.out.println("Received peers via new WebSocketMessage.");
      sendWaitingMessageToPeers(webSocketMessage);
    }
  }

  private void sendWaitingMessageToPeers(WebSocketMessage webSocketMessage) {
    String jsonMessage = messageWaitingRoom.remove(webSocketMessage.waitingMessageId);
    Gson gson = new Gson();
    webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);
    sendMessageToPeers(webSocketMessage, jsonMessage);
  }

  private String generateWaitingMessageId() {
    byte[] array = new byte[256]; // length is bounded by 7
    new Random().nextBytes(array);
    return Hashing.sha256().hashBytes(array).toString();
  }
}
