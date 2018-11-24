package ch.anjo.chatter.tomp2p.helpers;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterUser;
import ch.anjo.chatter.tomp2p.TomP2pMessage;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Objects;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class DataSender {

  private static BaseFutureAdapter<FutureSend> printConfirmation(
      ChatterPeer myself, String jsonMessage) {
    return new BaseFutureAdapter<FutureSend>() {
      @Override
      public void operationComplete(FutureSend future) {
        JsonParser parser = new JsonParser();
        JsonObject message = parser.parse(jsonMessage).getAsJsonObject();
        message.addProperty("received", true);
      }
    };
  }

  public static void sendWithConfirmation(ChatterPeer myself, String receiver, String jsonMessage) {
    sendWithListener(myself, receiver, jsonMessage, printConfirmation(myself, jsonMessage));
  }

  public static void sendToAllWithoutConfirmation(ChatterPeer chatterPeer, String outboundMessage) {
    Peer myself = chatterPeer.getMyself();
    ChatterUser chatterUser = chatterPeer.getChatterUser();
    PeerDHT dht = chatterPeer.getDht();

    chatterUser
        .getFriends()
        .forEach(
            friend ->
                dht.get(ChatterUser.getHash(friend))
                    .start()
                    .addListener(
                        new BaseFutureAdapter<FutureGet>() {
                          @Override
                          public void operationComplete(FutureGet future) {
                            Data data = future.data();
                            if (!data.isEmpty()) {
                              ChatterUser friend = readUser(data);
                              TomP2pMessage tomP2pMessage =
                                  new TomP2pMessage(
                                      chatterUser.getUsername(),
                                      Objects.requireNonNull(friend).getUsername(),
                                      outboundMessage);
                              myself
                                  .sendDirect(friend.getPeerAddress())
                                  .object(tomP2pMessage)
                                  .start()
                                  .addListener(
                                      new BaseFutureAdapter<BaseFuture>() {
                                        @Override
                                        public void operationComplete(BaseFuture baseFuture) {
                                          Data peerResponse = future.data();
                                          if (!peerResponse.isEmpty()) {
                                            Gson gson = new Gson();
                                            WebSocketMessage webSocketMessage =
                                                gson.fromJson(
                                                    tomP2pMessage.getJsonMessage(),
                                                    WebSocketMessage.class);

                                            if (webSocketMessage.type.equals(
                                                MessageTypes.GET_PEERS)) {
                                              chatterPeer.addFriend(friend.getUsername());
                                            }
                                          }
                                        }
                                      });
                            }
                          }
                        }));
  }

  private static void sendWithListener(
      ChatterPeer myself, String receiver, String jsonMessage, BaseFutureAdapter listener) {
    ChatterUser chatterUser = myself.getChatterUser();

    Data friendData = myself.getDht().get(ChatterUser.getHash(receiver)).start().awaitUninterruptibly().data();
    if (friendData == null) {
      return;
    }
    ChatterUser friendUser = DataSender.readUser(friendData);
    if (friendUser == null) {
      return;
    }
    TomP2pMessage tomP2pMessage =
        new TomP2pMessage(chatterUser.getUsername(), friendUser.getUsername(), jsonMessage);

    myself
        .getDht()
        .send(friendUser.getHash())
        .object(tomP2pMessage)
        .requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
        .start()
        .addListener(listener);
  }

  private static ChatterUser readUser(Data data) {
    try {
      return (ChatterUser) data.object();
    } catch (Exception e) {
      return null;
    }
  }

  public static HashCode getSha1Hash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  public static Number160 getHash(String otherUsername) {
    return new Number160(getSha1Hash(otherUsername).asBytes());
  }
}
