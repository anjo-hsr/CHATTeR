package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.helpers.DataSender;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
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

  public static BootstrapBuilder createBootStrapBuilder(ChatterPeer chatterPeer, Peer master) {
    Peer myself = chatterPeer.getMyself();
    ChatterUser chatterUser = chatterPeer.getChatterUser();
    PeerDHT dht = chatterPeer.getDht();

    BootstrapBuilder bootstrapBuilder = myself.bootstrap();

    System.out.println("Start peering with master:");
    System.out.println("Master with peerID - " + master.peerID() + " - at: " + master.peerAddress());

    bootstrapBuilder
        .bootstrapTo(Collections.singletonList(chatterPeer.getMasterAddress()))
        .start()
        .addListener(
            new BaseFutureAdapter<BaseFuture>() {
              @Override
              public void operationComplete(BaseFuture baseFuture) throws Exception {
                dht.get(myself.peerID()).start().addListener(
                    new BaseFutureAdapter<FutureGet>() {
                      @Override
                      public void operationComplete(FutureGet future) throws Exception {
                        Data data = future.data();
                        if (data != null) {
                          System.out.println("Friends received" + ((ChatterUser) data.object()).getFriends());
                          chatterUser.addFriends(((ChatterUser) data.object()).getFriends());
                        }
                        chatterUser.setOnlineState(true);
                        dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                        System.out.println("Connected to " + chatterPeer.getMasterName());
                        DataSender.sendToAllWithoutConfirmation(chatterPeer, generateGetPeerMessage());
                      }
                    }
                );
              }
            }
        );

    return bootstrapBuilder;
  }

  private static String generateGetPeerMessage() {
    JsonObject getPeers = new JsonObject();
    getPeers.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.GET_PEERS);
    getPeers.addProperty("message", "");
    return getPeers.toString();
  }

  public static void replyToData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<TomP2pMessage> messageHistory = chatterPeer.getMessageHistory();
    myself.objectDataReply((sender, request) -> {
      TomP2pMessage tomP2pMessage = (TomP2pMessage) request;

      Gson gson = new Gson();
      WebSocketMessage webSocketMessage = gson.fromJson(tomP2pMessage.getJsonMessage()
          , WebSocketMessage.class);

      if (webSocketMessage.type.equals(MessageTypes.GET_PEERS)) {
        ChannelAction.getFriends(chatterPeer);
        chatterPeer.addFriend(tomP2pMessage.getSender());
        return null;
      }

      if (!webSocketMessage.type.equals(MessageTypes.ADD_MESSAGE)) {
        webSocketClient.send(tomP2pMessage.getJsonMessage());
        return null;
      }

      if (messageHistory.indexOf(tomP2pMessage) == -1) {
        messageHistory.add(tomP2pMessage);
        // Notary service needed
      }

      Stream<TomP2pMessage> unverifiedMessages =
          messageHistory.stream().filter(Predicate.not(TomP2pMessage::isVerified));
      if (unverifiedMessages.count() > 0
          && tomP2pMessage.getJsonMessage().startsWith("WebSocketMessage recieved: ")) {
        String from = tomP2pMessage.getSender();
        // Send notary --> Approved that message has received
      }

      webSocketClient.send(tomP2pMessage.getJsonMessage());

      return null;
    });
  }

  public static String getFriends(ChatterPeer chatterPeer) {
    JsonObject responseJson = new JsonObject();
    responseJson.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();
    chatterPeer.getChatterUser().getFriends().forEach(peers::add);

    return responseJson.toString();
  }
}
