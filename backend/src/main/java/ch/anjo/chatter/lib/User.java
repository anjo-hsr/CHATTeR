package ch.anjo.chatter.lib;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

public class User implements Serializable {
  private final String email;
  // I figure this needs to be mutable... not nice.
  // Testing replacement for now
  private final String ip;
  private final int port;
  private List<String> friends;

  public User(String email, int port) throws SocketException, UnknownHostException {
    this(email, port, null);
  }

  public User(String email, int port, List<String> friends)
      throws UnknownHostException, SocketException {
    this.email = email;
    this.port = port;
    this.friends = new ArrayList<>();

    try (final DatagramSocket socket = new DatagramSocket()) {
      socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      ip = socket.getLocalAddress().getHostAddress();
    }
  }

  public HashCode getHash() {
    return Hashing.sha1().hashBytes(email.getBytes());
  }

  public Number160 get160Hash() {
    return new Number160(getHash().asBytes());
  }

  public void addFriend(String email) {
    friends.add(email);
  }

  public String getEmail() {
    return email;
  }

  public PeerAddress getAddress() throws UnknownHostException {
    return new PeerAddress(this.get160Hash(), this.ip, this.port, this.port);
  }

  public List<String> getFriendEmails() {
    return friends;
  }

  public void addFriends(List<String> friends) {
    if (friends != null) this.friends.addAll(friends);
  }

  public PeerAddress getAsPeerAddress() {
    try {
      return new PeerAddress(this.get160Hash(), this.ip, this.port, this.port);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "User{"
        + "email='"
        + email
        + '\''
        + ", ip='"
        + ip
        + '\''
        + ", port="
        + port
        + ", friends="
        + friends
        + ", hash="
        + this.get160Hash()
        + '}';
  }
}
