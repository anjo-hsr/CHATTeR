package ch.anjo.chatter.lib;

import ch.anjo.chatter.http.Parameters;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import net.tomp2p.peers.Number160;

public class ChatterUser implements Serializable {

  private final String username;
  private final String etherAddress;
  private String ipAddress;
  private final int port;
  private List<String> friends;
  private boolean isOnline;

  public ChatterUser(Parameters parameters) {
    this(parameters, new ArrayList<>());
  }

  public ChatterUser(Parameters parameters, ArrayList<String> friends) {
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
      System.err.println("Problems occurred by getting IP address of default route interface.");
    }
  }

  public HashCode getHash(String hashString) {
    return Hashing.sha1().hashBytes(hashString.getBytes());
  }

  public Number160 usernameToByteHash() {
    return new Number160(getHash(username).asBytes());
  }

  public void addFriend(String username) {
    friends.add(username);
  }
}
