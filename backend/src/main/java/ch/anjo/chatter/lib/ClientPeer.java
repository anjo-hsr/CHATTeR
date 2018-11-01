package ch.anjo.chatter.lib;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

public class ClientPeer {

  private final Peer me;
  private final PeerDHT dht;
  private final User user;

  /**
   * Creates a 'master' peer (with ID 1 for now) that starts to listen on port.
   *
   * @param port Port to start listening on
   * @throws IOException
   */
  public ClientPeer(String email, int port) throws IOException {
    byte[] hash = emailHash(email).asBytes();
    me = new PeerBuilder(new Number160(hash)).ports(port).start();
    dht = new PeerBuilderDHT(me).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.user = new User(email, port);
    dht.put(new Number160(user.getHash().asBytes())).data(new Data(user)).start();
  }

  /** Creates a 'slave' peer, and must be initialized with information about master */
  public ClientPeer(String masterEmail, String masterIp, String myEmail, int myPort)
      throws IOException {
    var master =
        new PeerBuilder(new Number160(emailHash(masterEmail).asBytes())).ports(myPort).start();
    me = new PeerBuilder(new Number160(emailHash(myEmail).asBytes())).masterPeer(master).start();
    dht = new PeerBuilderDHT(me).storageLayer(new StorageLayer(new StorageMemory())).start();

    this.user = new User(myEmail, myPort);

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
                            dht.put(user.get160Hash()).data(new Data(user)).start();
                            System.out.println(String.format("Connected to %s", masterEmail));
                            me.broadcast(new Number160(new Random().nextInt())).start();
                          }
                        });
              }
            });
  }

  public void disconnect() {
    me.announceShutdown().start().awaitUninterruptibly();
    me.shutdown().awaitUninterruptibly();
    dht.shutdown().awaitUninterruptibly();
  }

  private static HashCode emailHash(String email) {
    return Hashing.sha1().hashBytes(email.getBytes());
  }

  private static Number160 byteHash(String email) {
    return new Number160(emailHash(email).asBytes());
  }

  public void sendAll(String message) throws IOException, ClassNotFoundException {
    sendAllWithListener(message, nullConfirmation());
  }

  public void sendAllWithConfirmation(String message) throws IOException, ClassNotFoundException {
    sendAllWithListener(message, printConfirmation());
  }

  private void sendAllWithListener(String message, BaseFutureAdapter listener)
      throws IOException, ClassNotFoundException {

    user.getFriendEmails()
        .stream()
        .map(friend -> dht.get(byteHash(friend)).start().awaitUninterruptibly().data())
        .filter(Objects::nonNull)
        .map(ClientPeer::readUser)
        .filter(Objects::nonNull)
        .forEach(
            other ->
                me.sendDirect(other.getAsPeerAddress())
                    .object(new PeerMessage(user.getEmail(), other.getEmail(), message))
                    .start());
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
            f ->
                dht.send(f.get160Hash())
                    .object(new PeerMessage(user.getEmail(), f.getEmail(), message))
                    .requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
                    .start()
                    .addListener(listener));
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
          System.out.println(String.format("%s: %s", message.getFrom(), message.getContent()));
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
