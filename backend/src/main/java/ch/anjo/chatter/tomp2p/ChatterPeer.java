package ch.anjo.chatter.tomp2p;

import ch.anjo.chatchain.Constants;
import ch.anjo.chatchain.NotaryService;
import ch.anjo.chatter.tomp2p.helpers.TomP2pMessage;
import ch.anjo.chatter.tomp2p.parameters.ClientParameters;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.util.ArrayList;
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

public class ChatterPeer {

  private final Peer myself;
  private final PeerDHT dht;
  private final ChatterUser chatterUser;
  private List<TomP2pMessage> messageHistory = new ArrayList<>();
  private final String masterName;
  private PeerAddress masterAddress = null;
  private final NotaryService notaryService;

  public ChatterPeer(Parameters parameters) throws IOException {
    this.myself =
        new PeerBuilder(byteHash(parameters.getUsername()))
            .ports(parameters.getListeningPort())
            .start();

    this.dht =
        new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.masterName = parameters.getUsername();
    notaryService = new NotaryService(parameters.getUsername());

    this.chatterUser = new ChatterUser(parameters, Constants.walletMap.get(parameters.getUsername()));
    dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();

    System.out.println("Master service started:");
    System.out.println(
        String.format("Master with peerID - %s - at: %s", myself.peerID(), myself.peerAddress()));
  }

  public ChatterPeer(ClientParameters parameters) throws IOException {
    Validator validator = new Validator(parameters);
    ChatterAddress rendezvousChatterAddress = validator.getRendezvousChatterAddress();

    this.masterName = rendezvousChatterAddress.getUsername();
    String masterAddress = validator.getRendezvousChatterAddress().getHost();
    int masterPort = validator.getRendezvousChatterAddress().getPort();

    Peer master =
        new PeerBuilder(byteHash(this.masterName)).ports(parameters.getListeningPort()).start();
    this.masterAddress = new PeerAddress(master.peerID(), masterAddress, masterPort, masterPort);

    notaryService = new NotaryService(parameters.getUsername());
    this.myself = new PeerBuilder(byteHash(parameters.getUsername())).masterPeer(master).start();
    this.dht =
        new PeerBuilderDHT(this.myself).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.chatterUser = new ChatterUser(parameters, Constants.walletMap.get(parameters.getUsername()));
    ChannelAction.createBootStrapBuilder(this, master);
  }

  public Peer getMyself() {
    return myself;
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

  String getMasterName() {
    return masterName;
  }

  PeerAddress getMasterAddress() {
    return masterAddress;
  }

  private HashCode getHash(String username) {
    return Hashing.sha1().hashBytes(username.getBytes());
  }

  private Number160 byteHash(String username) {
    return new Number160(getHash(username).asBytes());
  }

  public NotaryService getNotaryService() {
    return notaryService;
  }

  public void addFriend(String username) {
    if (chatterUser.getUsername().equals(username)) {
      return;
    }
    dht.get(byteHash(username))
        .start()
        .addListener(
            new BaseFutureAdapter<FutureGet>() {
              @Override
              public void operationComplete(FutureGet future) {
                if (future.isSuccess()) {
                  Data friendData = future.data();
                  try {
                    if (friendData != null) {
                      ChatterUser friend = (ChatterUser) friendData.object();
                      chatterUser.addFriend(friend.getUsername());

                      dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
                    }
                  } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace();
                    System.out.println("Problem by detecting new friends.");
                  }
                }
              }
            });
  }

  public static ChatterUser readUser(Data data) {
    try {
      return (ChatterUser) data.object();
    } catch (Exception e) {
      return null;
    }
  }

  public void newNumberArrived() {
    System.out.println("--Phone booth is ringing...--");
    System.out.println("machine: CAN.YOU.HEAR.ME?");
    System.out.println(String.format("%s: Ohhh hell yeah", chatterUser.getUsername()));
    System.out.println(String.format("%s: Guys, we have a new number, I'll be right back.", chatterUser.getUsername()));

    terminate();
  }

  public void lastWords() {
    System.out.println("--Phone booth is ringing...--");
    System.out.println("--Recorder starts playing...--");
    System.out.println(String.format("%s: If you can hear this, you’re alone. The only thing left of me is the sound"
            + "of my voice. I don’t know if any of us made it. So let me tell you who we were. And how we fought back.",
        chatterUser.getUsername()));
    terminate();
  }

  private void terminate() {
    chatterUser.setOnlineState(false);

    try {
      dht.put(chatterUser.getHash()).data(new Data(chatterUser)).start();
    } catch (IOException e) {
      e.printStackTrace();
    }

    myself.announceShutdown().start().awaitUninterruptibly();
    myself.shutdown().awaitUninterruptibly();
    dht.shutdown().awaitUninterruptibly();
  }
}
