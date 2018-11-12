package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.tomp2p.parameters.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

public class ChatterUser implements Serializable {

  private final String username;
  private final String etherAddress;
  private String ipAddress;
  private final int port;
  private Set<String> friends;
  private boolean isOnline;

  public ChatterUser(Parameters parameters) {
    this(parameters, new HashSet<>());
  }

  private ChatterUser(Parameters parameters, Set<String> friends) {
    this.username = parameters.getUsername();
    this.etherAddress = parameters.getEtherAddress();
    this.port = parameters.getListeningPort();
    this.friends = friends;
    this.isOnline = true;

    // https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
    try (final DatagramSocket socket = new DatagramSocket()) {
      socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      ipAddress = socket.getLocalAddress().getHostAddress();
    } catch (IOException e) {
      System.err.println("Problems occurred by getting IP address of the default route interface.");
    }
  }

  public String getUsername() {
    return username;
  }

  public Set<String> getFriends() {
    return friends;
  }

  public boolean isOnline() {
    return isOnline;
  }

  private HashCode getSha1Hash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  public Number160 getHash() {
    return new Number160(getSha1Hash(username).asBytes());
  }

  public Number160 getHash(String otherUsername) {
    return new Number160(getSha1Hash(otherUsername).asBytes());
  }

  void addFriend(String username) {
    friends.add(username);
  }

  void addFriends(Set<String> newFriends) {
    if (friends != null) {
      friends.addAll(newFriends);
    }
  }

  void setOnlineState(boolean onlineState) {
    isOnline = onlineState;
  }

  public PeerAddress getPeerAddress() {
    try {
      return new PeerAddress(this.getHash(), this.ipAddress, this.port, this.port);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
