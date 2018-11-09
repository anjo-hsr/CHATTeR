package ch.anjo.chatter.lib;

import ch.anjo.chatter.http.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.StorageLayer;
import net.tomp2p.dht.StorageMemory;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ChatterPeer {

  private final Peer myself;
  private final PeerDHT peerDht;
  private final ChatterUser chatterUser;
  private List<ChatterMessage> messageHistory = new ArrayList<>();

  public ChatterPeer(Parameters parameters) throws IOException, NoSuchAlgorithmException {
    Peer master = null;

    if (parameters.getMode().equals("client")) {
      Validator validator = new Validator(parameters);
      ChatterAddress chatterAddress = validator.getChatterAddress();

      master =
          new PeerBuilder(byteHash(chatterAddress.getUsername()))
              .ports(parameters.getListeningPort())
              .start();
      this.myself =
          new PeerBuilder(byteHash(parameters.getUsername()))
              .masterPeer(master)
              .ports(chatterAddress.getPort())
              .start();
    } else {
      this.myself =
          new PeerBuilder(byteHash(parameters.getUsername()))
              .ports(parameters.getListeningPort())
              .start();
    }

    this.peerDht =
        new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();
    this.chatterUser = new ChatterUser(parameters);
    FuturePut futurePut =
        peerDht.put(chatterUser.usernameToByteHash()).data(new Data(chatterUser)).start();
    futurePut.awaitUninterruptibly();

    if (parameters.getMode().equals("master")) {
      return;
    }

    startBootstrapping(master);
  }

  private void startBootstrapping(Peer master) {
    FutureDiscover futureDiscover = myself.discover().peerAddress(master.peerAddress()).start();
    futureDiscover.awaitUninterruptibly();
    FutureBootstrap futureBootstrap = myself.bootstrap().peerAddress(master.peerAddress()).start();
    futureBootstrap.awaitUninterruptibly();
  }

  private HashCode getHash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  private Number160 byteHash(String hashString) {
    return new Number160(getHash(hashString).asBytes());
  }

  public void replyToData() {
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

          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          System.out.println(
              dateFormat.format(new Date())
                  + ": "
                  + myself.peerID().shortValue()
                  + " recieved message from "
                  + sender.peerId().shortValue()
                  + ".");

          return null;
        });
  }

  public void addPeer(String username) throws IOException, ClassNotFoundException {
    Data possibleFriend = peerDht.get(byteHash(username)).start().awaitUninterruptibly().data();
    if (possibleFriend != null) {
      ChatterUser friend = (ChatterUser) possibleFriend.object();
      chatterUser.addFriend(username);
      peerDht.put(chatterUser.usernameToByteHash()).data(new Data(chatterUser)).start();
    }
  }
}
