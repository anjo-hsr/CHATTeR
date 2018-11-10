package ch.anjo.chatter.tomp2p;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.builder.BootstrapBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

public class ChannelAction {

  public static BootstrapBuilder createBootStrapBuilder(ChatterPeer chatterPeer, Peer master) {
    Peer myself = chatterPeer.getMyself();
    ChatterUser chatterUser = chatterPeer.getChatterUser();
    PeerDHT peerDht = chatterPeer.getPeerDht();

    BootstrapBuilder bootstrapBuilder = myself.bootstrap();

    bootstrapBuilder
        .bootstrapTo(Collections.singleton(master.peerAddress()))
        .start()
        .addListener(
            new BaseFutureAdapter<BaseFuture>() {
              @Override
              public void operationComplete(BaseFuture baseFuture) throws Exception {
                peerDht.get(myself.peerID())
                    .start()
                    .addListener(
                        new BaseFutureAdapter<FutureGet>() {
                          @Override
                          public void operationComplete(FutureGet future) throws Exception {
                            Data data = future.data();
                            if (data != null) {
                              chatterUser.addFriends(((ChatterUser) data.object()).getFriends());
                            }
                            chatterUser.setOnlineState(true);
                            peerDht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                            myself.broadcast(new Number160(new Random().nextInt())).start();
                          }
                        }
                    );
              }
            }
        );

    return bootstrapBuilder;
  }

  public static void replyToData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<ChatterMessage> messageHistory = chatterPeer.getMessageHistory();

    myself.objectDataReply(
        (sender, inboundMessage) -> {
          ChatterMessage message = (ChatterMessage) inboundMessage;

          if (messageHistory.indexOf(message) == -1) {
            messageHistory.add(message);
            // Notary service needed
          }

          Stream<ChatterMessage> unverifiedMessages =
              messageHistory.stream().filter(Predicate.not(ChatterMessage::isVerified));
          if (unverifiedMessages.count() > 0
              && message.getMessage().startsWith("Message recieved: ")) {
            String from = message.getSender();
            // Send notary --> Approved that message has received
          }

          webSocketClient.send(message.getMessage());

          return null;
        });
  }
}
