package ch.sbi.blockchat.lib;

import ch.sbi.blockchain.Constants;
import ch.sbi.blockchain.NotaryService;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.StorageLayer;
import net.tomp2p.dht.StorageMemory;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.p2p.builder.BootstrapBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import org.web3j.crypto.CipherException;

public class ClientPeer {

  private final Peer me;
  private final PeerDHT dht;
  private final User user;
  private final NotaryService notaryService;
  private final List<PeerMessage> history = new ArrayList<>();

  /**
   * Creates a 'master' peer (with ID 1 for now) that starts to listen on port.
   *
   * @param port Port to start listening on
   * @throws IOException
   */
  public ClientPeer(String email, String ethAddress, int port) throws IOException, CipherException {
    byte[] hash = emailHash(email).asBytes();
    me = new PeerBuilder(new Number160(hash)).ports(port).start();
    dht = new PeerBuilderDHT(me).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.user = new User(email, ethAddress, port);
    dht.put(new Number160(user.getHash().asBytes())).data(new Data(user)).start();
    notaryService = new NotaryService(this.user.getWallet(), Constants.DEFAULT_PASSWORD);
  }

  /** Creates a 'slave' peer, and must be initialized with information about master */
  public ClientPeer(
      String masterEmail, String ethAddress, String masterIp, String myEmail, int myPort)
      throws IOException, CipherException {
    var master =
        new PeerBuilder(new Number160(emailHash(masterEmail).asBytes())).ports(myPort).start();
    me = new PeerBuilder(new Number160(emailHash(myEmail).asBytes())).masterPeer(master).start();
    dht = new PeerBuilderDHT(me).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.user = new User(myEmail, ethAddress, myPort);
    notaryService = new NotaryService(this.user.getWallet(), Constants.DEFAULT_PASSWORD);
    BootstrapBuilder builder = me.bootstrap();
    String[] address = masterIp.split(":");
    builder
        .bootstrapTo(
            Collections.singletonList(
                new PeerAddress(
                    master.peerID(),
                    address[0],
                    Integer.valueOf(address[1]),
                    Integer.valueOf(address[1]))))
        .start()
        .addListener(
            new BaseFutureAdapter<BaseFuture>() {
              @Override
              public void operationComplete(BaseFuture future) throws Exception {
                dht.get(me.peerID())
                    .start()
                    .addListener(
                        new BaseFutureAdapter<FutureGet>() {
                          @Override
                          public void operationComplete(FutureGet future) throws Exception {
                            Data data = future.data();
                            if (data != null) {
                              user.addFriends(((User) data.object()).getFriendEmails());
                            }
                            user.setOnline(true);
                            dht.put(user.get160Hash()).data(new Data(user)).start();
                            System.out.println(String.format("Connected to %s", masterEmail));
                            me.broadcast(new Number160(new Random().nextInt())).start();
                            sendAll("Hey, I'm online.");
                          }
                        });
              }
            });
  }

  public void disconnect() {
    this.user.setOnline(false);

    try {
      dht.put(user.get160Hash()).data(new Data(user)).start();
    } catch (IOException e) {
      // noperino
    }

    me.announceShutdown().start().awaitUninterruptibly();
    me.shutdown().awaitUninterruptibly();
    dht.shutdown().awaitUninterruptibly();
    System.out.println("Successfully disconnected");
  }

  private static HashCode emailHash(String email) {
    return Hashing.sha1().hashBytes(email.getBytes());
  }

  private static Number160 byteHash(String email) {
    return new Number160(emailHash(email).asBytes());
  }

  public void sendAll(String message) throws IOException, ClassNotFoundException {
    sendAllWithListener(message);
  }

  public void sendAllWithConfirmation(String message) {
    throw new RuntimeException("Not implemented");
    //sendAllWithListener(message, printConfirmation());
  }

  private void sendAllWithListener(String message) {
    user.getFriendEmails()
        .stream()
        .map(
            f ->
                dht.get(byteHash(f))
                    .start()
                    .addListener(
                        new BaseFutureAdapter<FutureGet>() {
                          @Override
                          public void operationComplete(FutureGet future) throws Exception {
                            var data = future.data();
                            if (!data.isEmpty()) {
                              var other = ClientPeer.readUser(data);
                              var m = new PeerMessage(user.getEmail(), other.getEmail(), message);
                              me.sendDirect(other.getAsPeerAddress()).object(m).start();
                            }
                          }
                        }));
  }

  public void send(String other, String message) {
    sendWithListener(other, message, nullConfirmation());
  }

  public void sendWithConfirmation(String other, String message) {
    sendWithListener(other, message, printConfirmation());
  }

  private void sendWithListener(String other, String message, BaseFutureAdapter listener) {
    user.getFriendEmails()
        .stream()
        .filter(f -> f.equals(other))
        .map(f -> dht.get(byteHash(f)).start().awaitUninterruptibly().data())
        .filter(Objects::nonNull)
        .map(ClientPeer::readUser)
        .filter(Objects::nonNull)
        .forEach(
            f -> {
              var m = new PeerMessage(user.getEmail(), f.getEmail(), message);
              // history.add(m);
              dht.send(f.get160Hash())
                  .object(m)
                  .requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
                  .start()
                  .addListener(listener);
            });
  }

  public void addPeer(String email) throws IOException, ClassNotFoundException {
    Data other = dht.get(byteHash(email)).start().awaitUninterruptibly().data();
    if (other != null) {
      User found = (User) other.object();
      this.user.addFriend(found.getEmail());
      dht.put(user.get160Hash()).data(new Data(user)).start();
    } else {
      System.out.println(String.format("No peer found with nick name %s", email));
    }
  }

  public void addDataReply() {
    me.objectDataReply(
        (sender, request) -> {
          PeerMessage message = (PeerMessage) request;
          var received =
              history.stream().filter(m -> message.getContent().contains(m.getContent()));

          // we haven't received this message yet
          if (received.count() <= 0) {
            history.add(message);
            this.notaryService
                .storeMessageOnBlockchain(message.getContent())
                .thenRun(
                    () -> {
                      send(message.getFrom(), "I received the message: " + message.getContent());
                      System.out.println(
                          String.format("%s: %s", message.getFrom(), message.getContent()));
                    });
          }

          // we haven't verified this message yet
          var unverified =
              history
                  .stream()
                  .filter(m -> message.getContent().contains(m.getContent()))
                  .filter(m -> !m.isVerified());

          if (message.getContent().startsWith("I received the message: ")
              && unverified.count() > 0) {
            String from = User.ETH_WALLETS.get(message.getFrom());
            String m = message.getContent();

            var b = notaryService.verifySenderOfMessage(from, m).get();
            if (b && !message.getFrom().equals(user.getEmail())) {
              System.out.println(String.format("Verified that %s has received '%s'", from, m));
              unverified.forEach(x -> x.verify());
            }
          }

          return String.format("%s confirming from %s", me.peerID(), sender.peerId());
        });
  }

  private BaseFutureAdapter<FutureSend> printConfirmation() {
    return new BaseFutureAdapter<FutureSend>() {
      @Override
      public void operationComplete(FutureSend future) throws Exception {
        System.out.println(String.format("Receive confirmation of client %s", me.peerID()));
        Arrays.stream(future.rawDirectData2().values().toArray())
            .forEach(s -> System.out.println(String.format("\t%s", s)));
      }
    };
  }

  private BaseFutureAdapter<FutureSend> nullConfirmation() {
    return new BaseFutureAdapter<FutureSend>() {
      @Override
      public void operationComplete(FutureSend futureSend) throws Exception {}
    };
  }

  private static User readUser(Data data) {
    try {
      return (User) data.object();
    } catch (Exception e) {
      return null;
    }
  }

  public User getUser() {
    return user;
  }

  public String getEmail() {
    return user.getEmail();
  }
}
