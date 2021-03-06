package ch.anjo.chatter.tomp2p;

public class ChatterAddress {

  private final String username;
  private final String host;
  private final int port;

  ChatterAddress(String username, String host, int port) {
    this.username = username;
    this.host = host;
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  String getHost() {
    return host;
  }

  int getPort() {
    return port;
  }
}
