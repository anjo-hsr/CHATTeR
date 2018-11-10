package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.templates.Message;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

  public ChatterWebSocketClient(int webSocketPort, String username, ChatterPeer myself) throws URISyntaxException {
    super(new URI("ws://localhost:" + webSocketPort + "/chat?wsType=backend&username=" + username));

    this.myself = myself;
    this.username = username;
    this.chatMembers = new HashMap<>();
    this.messageWaitingRoom = new HashMap<>();
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
    Message message = gson.fromJson(jsonMessage, Message.class);

    switch (message.type) {
      case MessageTypes.ADD_MESSAGE:
        sendMessageToPeers(message, jsonMessage);
        break;
      case MessageTypes.SET_USERNAME:
        break;
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT:
        requestPeers(message, "");
        break;
      case MessageTypes.DELETE_CHAT:
        break;
      case MessageTypes.SELECT_CHAT:
        break;
      case MessageTypes.SEND_CHAT_PEERS:
        updateChatPeerMap(message);
        break;
      default:
        return;
    }
  }

  private void sendMessageToPeers(Message message, String jsonMessage) {
    if (chatMembers.containsKey(message.chatId)) {
      Set<String> chatPeers = chatMembers.get(message.chatId);
      System.out.println("Direct");
      chatPeers.forEach(peer -> DataSender.sendWithConfirmation(myself, peer, jsonMessage));
      return;
    }
    String messageId = generateMessageId(message);
    messageWaitingRoom.put(messageId, jsonMessage);

    requestPeers(message, messageId);
  }

  private void requestPeers(Message message, String messageId) {
    JsonObject getChatPeers = new JsonObject();
    getChatPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_CHAT_PEERS);
    getChatPeers.addProperty("chatId", message.chatId);
    getChatPeers.addProperty("id", messageId);
    this.send(getChatPeers.toString());
  }

  private void updateChatPeerMap(Message message) {
    String[] peers = message.peers;
    String[] otherPeers = Arrays.stream(peers).filter(peer -> !peer.equals(username)).toArray(String[]::new);
    HashSet<String> peerSet = new HashSet<>(Set.of(otherPeers));

    chatMembers.put(message.chatId, peerSet);

    if (message.id.equals("")) {
      System.out.println("Updated peers via Chat update");
    } else {
      System.out.println("Recieved peers via new message.");
      sendWaitingMessageToPeers(message);
    }
  }

  private void sendWaitingMessageToPeers(Message message) {
    String jsonMessage = messageWaitingRoom.remove(message.id);
    Gson gson = new Gson();
    message = gson.fromJson(jsonMessage, Message.class);
    sendMessageToPeers(message, jsonMessage);
  }

  private String generateMessageId(Message message) {
    byte[] array = new byte[256]; // length is bounded by 7
    new Random().nextBytes(array);
    return Hashing.sha256().hashBytes(array).toString();
  }

};