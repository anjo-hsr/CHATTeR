package ch.anjo.chatter.websocket;

public class WebUser {
  private final String name;
  private final int port;

  public WebUser(String name, String port) {
    this.name = name;
    this.port = Integer.valueOf(port);
  }

  public String getName() {
    return name;
  }

  public int getPort() {
    return port;
  }

  @Override
  public String toString() {
    return "ChatterUser{" + "name='" + name + '\'' + ", port='" + port + '\'' + '}';
  }
}
