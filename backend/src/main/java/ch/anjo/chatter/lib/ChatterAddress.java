package ch.anjo.chatter.lib;

public class ChatterAddress {
  private final String username;
  private final String host;
  private final int port;

  public ChatterAddress(String username, String host, int port) {
    this.username = username;
    this.host = host;
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}
