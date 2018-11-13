package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.builder.BootstrapBuilder;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

public class ChannelAction {

  public static void createBootStrapBuilder(ChatterPeer chatterPeer, Peer master) {
    Peer myself = chatterPeer.getMyself();
    ChatterUser chatterUser = chatterPeer.getChatterUser();
    PeerDHT dht = chatterPeer.getDht();

    BootstrapBuilder bootstrapBuilder = myself.bootstrap();

    System.out.println("Start peering with master:");
    System.out.println(
        "Master with peerID - " + master.peerID() + " - at: " + master.peerAddress());

    bootstrapBuilder
        .bootstrapTo(Collections.singletonList(chatterPeer.getMasterAddress()))
        .start()
        .addListener(
            new BaseFutureAdapter<BaseFuture>() {
              @Override
              public void operationComplete(BaseFuture baseFuture) {
                dht.get(myself.peerID())
                    .start()
                    .addListener(
                        new BaseFutureAdapter<FutureGet>() {
                          @Override
                          public void operationComplete(FutureGet future) throws Exception {
                            Data data = future.data();
                            if (data != null) {
                              System.out.println(
                                  "Friends received" + ((ChatterUser) data.object()).getFriends());

                              ((ChatterUser) data.object()).getFriends().stream()
                                  .filter(friend -> !friend.equals(chatterPeer.getChatterUser().getUsername()))
                                  .forEach(chatterUser::addFriend);
                            }
                            chatterUser.setOnlineState(true);
                            dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                            System.out.println("Connected to " + chatterPeer.getMasterName());
                            DataSender.sendToAllWithoutConfirmation(
                                chatterPeer, generateGetPeerMessage());
                          }
                        });
              }
            });
  }

  private static String generateGetPeerMessage() {
    JsonObject getPeers = new JsonObject();
    getPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_PEERS);
    return getPeers.toString();
  }

  public static void replyToData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<TomP2pMessage> messageHistory = chatterPeer.getMessageHistory();
    myself.objectDataReply(
        (sender, request) -> {
          TomP2pMessage tomP2pMessage = (TomP2pMessage) request;

          Gson gson = new Gson();
          WebSocketMessage webSocketMessage =
              gson.fromJson(tomP2pMessage.getJsonMessage(), WebSocketMessage.class);

          switch (webSocketMessage.type) {
            case MessageTypes.GET_PEERS: {
              if (!tomP2pMessage.getSender().equals(chatterPeer.getChatterUser().getUsername())) {
                String responseJson = ChannelAction.getFriends(chatterPeer);
                chatterPeer.addFriend(tomP2pMessage.getSender());

                return responseJson;
              }
              break;
            }
            case MessageTypes.ADD_CHAT:
            case MessageTypes.CHANGE_CHAT: {
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              return null;
            }
            default:
              if (!webSocketMessage.type.equals(MessageTypes.ADD_MESSAGE)) {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
                return null;
              }
          }

          if (messageHistory.stream().anyMatch(message ->
              webSocketMessage.messageInformation.author.equals(chatterPeer.getChatterUser().getUsername()) ||
                  message.getJsonMessage().equals(tomP2pMessage.getJsonMessage()))) {
            return null;
          } else {
            messageHistory.add(tomP2pMessage);
            // Notary service needed
          }

          Stream<TomP2pMessage> unverifiedMessages =
              messageHistory.stream().filter(Predicate.not(TomP2pMessage::isVerified));
          if (webSocketMessage.type.equals(MessageTypes.CONFIRM_MESSAGE)) {
            String from = tomP2pMessage.getSender();
            String etherAddress;
            // Send notary --> Approved that message has received
            return null;
          }

          return getConfirmMessage(webSocketClient, tomP2pMessage, webSocketMessage);
        });
  }

  private static Object getConfirmMessage(WebSocketClient webSocketClient, TomP2pMessage tomP2pMessage,
      WebSocketMessage webSocketMessage) {
    JsonObject response = new JsonObject();
    response.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.CONFIRM_MESSAGE);
    response.addProperty("messageId", webSocketMessage.messageInformation.messageId);

    webSocketClient.send(tomP2pMessage.getJsonMessage());

    return response.toString();
  }

  private static String getFriends(ChatterPeer chatterPeer) {
    JsonObject responseJson = new JsonObject();
    responseJson.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();
    Set<String> friends = chatterPeer.getChatterUser().getFriends();

    Set<JsonObject> peerSet = friends.stream()
        .map(friend -> chatterPeer.getDht().get(ChatterUser.getHash(friend))
            .start()
            .awaitUninterruptibly()
            .data())
        .filter(Objects::nonNull)
        .map(ChatterPeer::readUser)
        .filter(Objects::nonNull)
        .map(ChatterUser::getInformation).collect(Collectors.toSet());

    peerSet.forEach(peers::add);
    responseJson.add("peers", peers);

    return responseJson.toString();
  }
}
