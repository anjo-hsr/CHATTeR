package ch.anjo.chatter.tomp2p;

import ch.anjo.chatchain.Address;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
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
  private String ipAddress;
  private final int port;
  private Set<String> friends;
  private boolean isOnline;
  private final Address etherAddress;

  ChatterUser(Parameters parameters, Address etherAddress) {
    this(parameters,etherAddress, new HashSet<>());
  }

  private ChatterUser(Parameters parameters,Address etherAddress, Set<String> friends) {
    this.username = parameters.getUsername();
    this.port = parameters.getListeningPort();
    this.friends = friends;
    this.isOnline = true;
    this.etherAddress = etherAddress;

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

  boolean isOnline() {
    return isOnline;
  }

  private HashCode getSha1Hash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  public Number160 getHash() {
    return new Number160(getSha1Hash(username).asBytes());
  }

  public static Number160 getHash(String otherUsername) {
    return new Number160(Hashing.sha1().hashBytes(otherUsername.getBytes()).asBytes());
  }

  void addFriend(String username) {
    friends.add(username);
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

  public JsonObject getInformation() {
    JsonObject response = new JsonObject();
    response.addProperty("name", username);
    response.addProperty("isOnline", isOnline);
    response.addProperty("etherAddress", etherAddress.getAddress());
    return response;
  }
}
