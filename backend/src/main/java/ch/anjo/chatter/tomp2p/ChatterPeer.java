package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.tomp2p.parameters.ClientParameters;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.StorageLayer;
import net.tomp2p.dht.StorageMemory;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

public class ChatterPeer {

  private final Peer myself;
  private final PeerDHT peerDht;
  private final ChatterUser chatterUser;
  private List<ChatterMessage> messageHistory = new ArrayList<>();

  public ChatterPeer(Parameters parameters) throws IOException {
    this.myself =
        new PeerBuilder(byteHash(parameters.getUsername()))
            .ports(parameters.getListeningPort())
            .start();

    this.peerDht =
        new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();
    this.chatterUser = new ChatterUser(parameters);
    setFuturePut();

  }

  public ChatterPeer(ClientParameters parameters) throws IOException {
    Validator validator = new Validator(parameters);
    ChatterAddress chatterAddress = validator.getChatterAddress();

    Peer master = new PeerBuilder(byteHash(chatterAddress.getUsername()))
        .ports(parameters.getListeningPort())
        .start();

    this.myself = new PeerBuilder(byteHash(parameters.getUsername()))
        .masterPeer(master)
        .ports(chatterAddress.getPort())
        .start();

    this.peerDht = new PeerBuilderDHT(this.myself)
        .storageLayer(new StorageLayer(new StorageMemory()))
        .start();

    this.chatterUser = new ChatterUser(parameters);

    setFuturePut();

    ChannelAction.createBootStrapBuilder(this, master);
  }

  private void setFuturePut() throws IOException {
    FuturePut futurePut =
        peerDht.put(chatterUser.usernameToByteHash()).data(new Data(chatterUser)).start();
    futurePut.awaitUninterruptibly();
  }

  public Peer getMyself() {
    return myself;
  }

  public PeerDHT getPeerDht() {
    return peerDht;
  }

  public ChatterUser getChatterUser() {
    return chatterUser;
  }

  public List<ChatterMessage> getMessageHistory() {
    return messageHistory;
  }

  private HashCode getHash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  private Number160 byteHash(String hashString) {
    return new Number160(getHash(hashString).asBytes());
  }

  public void replyToData(WebSocketClient webSocketClient) {
    ChannelAction.replyToData(this, webSocketClient);
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
