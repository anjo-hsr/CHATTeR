package ch.anjo.chatter.cli;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import net.tomp2p.peers.Number160;

public class User implements Serializable {
  private final String email;
  // I figure this needs to be mutable... not nice.
  // Testing replacement for now
  private final String ip;
  private final int port;

  public User(String email, int port) throws UnknownHostException, SocketException {
    this.email = email;
    this.port = port;
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

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "User{" + "email='" + email + '\'' + ", ip='" + ip + '\'' + ", port=" + port + '}';
  }
}
