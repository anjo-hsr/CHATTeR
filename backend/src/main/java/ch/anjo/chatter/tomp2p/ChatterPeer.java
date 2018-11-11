package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.parameters.ClientParameters;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.StorageLayer;
import net.tomp2p.dht.StorageMemory;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

public class ChatterPeer {

  private final Peer myself;
  private final PeerDHT dht;
  private final ChatterUser chatterUser;
  private List<TomP2pMessage> messageHistory = new ArrayList<>();
  private final String masterName;
  private PeerAddress masterAddress = null;

  public ChatterPeer(Parameters parameters) throws IOException {
    this.myself =
        new PeerBuilder(byteHash(parameters.getUsername()))
            .ports(parameters.getListeningPort())
            .start();

    this.dht =
        new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();
    this.chatterUser = new ChatterUser(parameters);

    dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();

    this.masterName = parameters.getUsername();

    System.out.println("Master service started:");
    System.out.println("Master with peerID - " + myself.peerID() + " - at: " + myself.peerAddress());
  }

  public ChatterPeer(ClientParameters parameters) throws IOException {
    Validator validator = new Validator(parameters);
    ChatterAddress rendezvousChatterAddress = validator.getRendezvousChatterAddress();

    this.masterName = rendezvousChatterAddress.getUsername();
    String masterAddress = validator.getRendezvousChatterAddress().getHost();
    int masterPort = validator.getRendezvousChatterAddress().getPort();

    Peer master = new PeerBuilder(byteHash(this.masterName)).ports(parameters.getListeningPort()).start();
    this.masterAddress = new PeerAddress(master.peerID(), masterAddress, masterPort, masterPort);

    this.myself = new PeerBuilder(byteHash(parameters.getUsername())).masterPeer(master).start();
    this.dht = new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.chatterUser = new ChatterUser(parameters);
    ChannelAction.createBootStrapBuilder(this, master);
  }

  public Peer getMyself() {
    return myself;
  }

  public Number160 getPeerId() {
    return myself.peerID();
  }

  public PeerDHT getDht() {
    return dht;
  }

  public ChatterUser getChatterUser() {
    return chatterUser;
  }

  public List<TomP2pMessage> getMessageHistory() {
    return messageHistory;
  }

  public String getMasterName() {
    return masterName;
  }

  public PeerAddress getMasterAddress() {
    return masterAddress;
  }

  private HashCode getHash(String username) {
    return Hashing.sha1().hashBytes(username.getBytes());
  }

  private Number160 byteHash(String username) {
    return new Number160(getHash(username).asBytes());
  }

  public void replyToData(WebSocketClient webSocketClient) {
    ChannelAction.replyToData(this, webSocketClient);
  }

  public void addFriend(String username) {
    dht.get(byteHash(username)).start().addListener(new BaseFutureAdapter<FutureGet>() {
      @Override
      public void operationComplete(FutureGet future) throws Exception {
        if (future.isSuccess()) {
          Data friendData = future.data();
          if (!friendData.isEmpty()) {
            ChatterUser friend = (ChatterUser) friendData.object();
            chatterUser.addFriend(friend.getUsername());

            dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
          }
        }
      }
    });

  }

  private String generateAddPeers(String friend) {
    return generateAddPeers(Collections.singletonList(friend));
  }

  private String generateAddPeers(List<String> friends) {
    JsonObject addPeer = new JsonObject();
    addPeer.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);
    JsonArray peers = new JsonArray();
    friends.forEach(peers::add);

    addPeer.add("peers", peers);
    return addPeer.toString();
  }
}
