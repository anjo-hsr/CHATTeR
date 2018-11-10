package ch.anjo.chatter.tomp2p.parameters;

import java.util.Arrays;

public class Parameters {

  private final String mode;
  private final String username;
  private final String etherAddress;
  private final int listeningPort;
  private final int webSocketPort;
  private final int frontendPort;

  public Parameters(String args[]) {
    this.mode = getMode(args[0]);
    this.username = args[1];
    this.etherAddress = args[2];
    this.listeningPort = args.length >= 4 ? Integer.valueOf(args[3]) : 5000;

    int index = indexOfRendezvousString(args);
    this.webSocketPort = index == 5 ? Integer.valueOf(args[4]) : listeningPort + 3000;
    this.frontendPort = getWebSocketPortNumber(args[0]);
  }

  private String getMode(String argMode) {
    return argMode.split(":")[0];
  }

  private int getWebSocketPortNumber(String argMode) {
    return isDevelopment(argMode) ? webSocketPort - 5000 : webSocketPort;
  }

  private boolean isDevelopment(String argMode) {
    String[] modeType = argMode.split(":");
    return modeType.length == 2 && modeType[1].equals("dev");
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
    return "";
  }

  public int indexOfRendezvousString(String[] args) {
    int index = 0;
    for (String arg : args) {
      if (arg.matches(".*@.*:.*")) {
        return index;
      }
      index += 1;
    }
    return -1;
  }

  public int getFrontendPort() {
    return frontendPort;
  }
}
