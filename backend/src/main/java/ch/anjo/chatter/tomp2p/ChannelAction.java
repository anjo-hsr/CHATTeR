package ch.anjo.chatter.tomp2p;

import static ch.anjo.chatter.tomp2p.ChatterPeer.readUser;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.helpers.JsonGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.p2p.builder.BootstrapBuilder;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

class ChannelAction {

  static void createBootStrapBuilder(ChatterPeer chatterPeer, Peer master) {
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
                            System.out.println(
                                String.format("Connected to %s", chatterPeer.getMasterName()));

                            Data data = future.data();
                            if (data != null) {
                              System.out.println(
                                  String.format(
                                      "%sFriends received %s",
                                      DateGenerator.getDate(),
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
                            DataSender.sendIAmOnline(
                                chatterPeer, JsonGenerator.generateAddPeers(chatterUser));
                            chatterUser.setOnlineState(true);
                            dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                          }
                        });
              }
            });
  }

  static void replyToTomP2PData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<TomP2pMessage> messageHistory = chatterPeer.getMessageHistory();
    myself.objectDataReply(
        (sender, request) -> {
          TomP2pMessage tomP2pMessage = (TomP2pMessage) request;

          Gson gson = new Gson();
          WebSocketMessage webSocketMessage =
              gson.fromJson(tomP2pMessage.getJsonMessage(), WebSocketMessage.class);

          switch (webSocketMessage.type) {
            case MessageTypes.ADD_PEERS: {
              if (!tomP2pMessage.getSender().equals(chatterPeer.getChatterUser().getUsername())) {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
              }
              break;
            }

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
              sendPeerInformation(chatterPeer, webSocketMessage);
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
            case MessageTypes.CONFIRM_MESSAGE: {
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
            case MessageTypes.ADD_MESSAGE: {
              if (messageHistory
                  .stream()
                  .anyMatch(
                      message ->
                          webSocketMessage.messageInformation.author.equals(
                              chatterPeer.getChatterUser().getUsername())
                              || message.getJsonMessage().equals(tomP2pMessage.getJsonMessage()))) {
                return "";
              }
              messageHistory.add(tomP2pMessage);
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              confirmMessage(chatterPeer, webSocketMessage, tomP2pMessage);
              break;
            }
            default: {
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
          }
          return "";
        });
  }

  private static void sendPeerInformation(ChatterPeer chatterPeer, WebSocketMessage webSocketMessage) {
    String response = JsonGenerator.generateAddPeers(chatterPeer.getChatterUser());
    ChatterUser chatterUser = chatterPeer.getChatterUser();
    webSocketMessage.chatInformation.peers.forEach(peer -> {
      chatterPeer.getDht()
          .get(ChatterUser.getHash(peer))
          .start().addListener(
          new BaseFutureAdapter<FutureGet>() {
            @Override
            public void operationComplete(FutureGet future) {
              Data data = future.data();
              if (Objects.nonNull(data) && !data.isEmpty()) {
                ChatterUser chatMember = readUser(data);
                TomP2pMessage tomP2pMessage = null;
                if (chatMember != null) {
                  tomP2pMessage =
                      new TomP2pMessage(
                          chatterUser.getUsername(), peer, response);

                  chatterPeer.getDht()
                      .send(chatMember.getHash())
                      .object(tomP2pMessage)
                      .requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
                      .start();

                }
              }
            }
          }
      );
    });
  }

  private static void confirmMessage(
      ChatterPeer chatterPeer, WebSocketMessage webSocketMessage, TomP2pMessage tomP2pMessage) {
    if (!webSocketMessage.messageInformation.author.equals(
        chatterPeer.getChatterUser().getUsername())) {
      String response = JsonGenerator.generateConfirmMessage(chatterPeer, webSocketMessage);
      System.out.println(tomP2pMessage.getSender());
      DataSender.sendWithConfirmation(chatterPeer, tomP2pMessage.getSender(), response);
    }
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
                          ChatterUser friend = readUser(data);
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
