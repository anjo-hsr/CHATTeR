package ch.anjo.chatter.http;

public class Parameters {

  private final String mode;
  private final String username;
  private final String etherAddress;
  private final int listeningPort;
  private final int webSocketPort;
  private final String rendezvousString;

  public Parameters(String args[]) {
    this.mode = args[0];
    this.username = args[1];
    this.etherAddress = args[2];
    this.listeningPort = args.length >= 4 ? Integer.valueOf(args[3]) : 5000;
    this.webSocketPort = args.length >= 5 ? Integer.valueOf(args[4]) : 8000;
    this.rendezvousString = args.length >= 6 ? args[5] : "";
  }

  public String getMode() {
    return mode;
  }

  public String getUsername() {
    return username;
  }

  public String getEtherAddress() {
    return etherAddress;
  }

  public int getListeningPort() {
    return listeningPort;
  }

  public int getWebSocketPort() {
    return webSocketPort;
  }

  public String getRendezvousString() {
    return rendezvousString;
  }
}
