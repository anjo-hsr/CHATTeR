package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.JsonGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
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
        String.format("Master with peerID - %s - at: %s", master.peerID(), master.peerAddress()));

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
                                  String.format(
                                      "Friends received %s",
                                      ((ChatterUser) data.object()).getFriends()));

                              ((ChatterUser) data.object())
                                  .getFriends()
                                  .stream()
                                  .filter(
                                      friend ->
                                          !friend.equals(
                                              chatterPeer.getChatterUser().getUsername()))
                                  .forEach(chatterUser::addFriend);
                            }
                            chatterUser.setOnlineState(true);
                            dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                            System.out.println(
                                String.format("Connected to %s", chatterPeer.getMasterName()));
                            DataSender.sendToAllWithoutConfirmation(
                                chatterPeer, JsonGenerator.generateGetPeer());
                          }
                        });
              }
            });
  }

  public static void replyToTomP2PData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<TomP2pMessage> messageHistory = chatterPeer.getMessageHistory();
    myself.objectDataReply(
        (sender, request) -> {
          TomP2pMessage tomP2pMessage = (TomP2pMessage) request;

          Gson gson = new Gson();
          WebSocketMessage webSocketMessage =
              gson.fromJson(tomP2pMessage.getJsonMessage(), WebSocketMessage.class);

          switch (webSocketMessage.type) {
            case MessageTypes.GET_PEERS:
              {
                if (!tomP2pMessage.getSender().equals(chatterPeer.getChatterUser().getUsername())) {
                  String responseJson = ChannelAction.getFriends(chatterPeer);
                  chatterPeer.addFriend(tomP2pMessage.getSender());

                  return responseJson;
                }
                break;
              }
            case MessageTypes.ADD_CHAT:
            case MessageTypes.CHANGE_CHAT:
              {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
                return null;
              }
            case MessageTypes.CONFIRM_MESSAGE:
              {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
                return null;
              }
            case MessageTypes.ADD_MESSAGE:
              {
                messageHistory.add(tomP2pMessage);
                webSocketClient.send(tomP2pMessage.getJsonMessage());
                confirmMessage(chatterPeer, webSocketMessage, tomP2pMessage);
              }
            default:
              {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
                return null;
              }
          }

          if (messageHistory
              .stream()
              .anyMatch(
                  message ->
                      webSocketMessage.messageInformation.author.equals(
                              chatterPeer.getChatterUser().getUsername())
                          || message.getJsonMessage().equals(tomP2pMessage.getJsonMessage()))) {
            return null;
          }
          // Notary service needed

          Stream<TomP2pMessage> unverifiedMessages =
              messageHistory.stream().filter(Predicate.not(TomP2pMessage::isVerified));
          if (webSocketMessage.type.equals(MessageTypes.CONFIRM_MESSAGE)) {
            String from = tomP2pMessage.getSender();
            String etherAddress;
            // Send notary --> Approved that message has received
            return null;
          }

          return null;
        });
  }

  private static void confirmMessage(
      ChatterPeer chatterPeer, WebSocketMessage webSocketMessage, TomP2pMessage tomP2pMessage) {
    String response = JsonGenerator.generateConfirmMessage(chatterPeer, webSocketMessage);
    System.out.println(tomP2pMessage.getSender());
    DataSender.sendWithConfirmation(chatterPeer, tomP2pMessage.getSender(), response);
  }

  private static String getFriends(ChatterPeer chatterPeer) {
    JsonObject responseJson = new JsonObject();
    responseJson.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();
    Set<String> friends = chatterPeer.getChatterUser().getFriends();

    Set<JsonObject> peerSet = new HashSet<>();
    friends.forEach(
        friend ->
            chatterPeer
                .getDht()
                .get(ChatterUser.getHash(friend))
                .start()
                .addListener(
                    new BaseFutureAdapter<FutureGet>() {
                      @Override
                      public void operationComplete(FutureGet future) {
                        Data data = future.data();
                        if (!data.isEmpty()) {
                          ChatterUser friend = ChatterPeer.readUser(data);
                          if (Objects.nonNull(friend)) {
                            peerSet.add(friend.getInformation());
                          }
                        }
                      }
                    }));

    responseJson.add("peers", peers);

    return responseJson.toString();
  }
}
