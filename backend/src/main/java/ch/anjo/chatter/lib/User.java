package ch.anjo.chatter.lib;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import org.web3j.crypto.CipherException;

public class User implements Serializable {
  public static final Map<String, String> ETH_WALLETS = new HashMap<>();

  static {
    ETH_WALLETS.put(
        "0x5e521aa5ab7f066bcda82457436f506a4cf2e3b5",
        "UTC--2018-10-27T14-21-33.994725408Z--5e521aa5ab7f066bcda82457436f506a4cf2e3b5");
    ETH_WALLETS.put(
        "0xa305db054e1626e465f2dad6471ef0ef738da928",
        "UTC--2018-10-27T14-22-11.548862555Z--a305db054e1626e465f2dad6471ef0ef738da928");
    ETH_WALLETS.put(
        "0x67e0f15200712951d990abb361ffacbfbc881f34",
        "UTC--2018-10-27T14-27-43.987946024Z--67e0f15200712951d990abb361ffacbfbc881f34");
    ETH_WALLETS.put(
        "0xcd471cad1231319384400f112a23445dfd44f95e",
        "UTC--2018-11-02T19-35-10.022201201Z--cd471cad1231319384400f112a23445dfd44f95e");

    ETH_WALLETS.put("martin", "0x5e521aa5ab7f066bcda82457436f506a4cf2e3b5");
    ETH_WALLETS.put("sbi", "0xa305db054e1626e465f2dad6471ef0ef738da928");
    ETH_WALLETS.put("tobi", "0x67e0f15200712951d990abb361ffacbfbc881f34");
    ETH_WALLETS.put("raffaela", "0xcd471cad1231319384400f112a23445dfd44f95e");
  }

  private final String email;
  // I figure this needs to be mutable... not nice.
  // Testing replacement for now
  private final String ip;
  private final int port;
  private List<String> friends;
  private boolean online;
  private final String wallet;
  private final String ethAddress;

  public User(String email, String ethAddress, int port) throws IOException, CipherException {
    this(email, ethAddress, port, null);
  }

  public User(String email, String ethAddress, int port, List<String> friends)
      throws IOException, CipherException {
    this.email = email;
    this.port = port;
    this.friends = new ArrayList<>();
    this.ethAddress = ethAddress;
    this.wallet = assignWallet(ethAddress);

    try (final DatagramSocket socket = new DatagramSocket()) {
      socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      ip = socket.getLocalAddress().getHostAddress();
    }
  }

  public String assignWallet(String ethAddress) {
    return ETH_WALLETS.get(ethAddress);
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

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public String getEthAddress() {
    return ethAddress;
  }

  public String getWallet() {
    return wallet;
  }
}
