package ch.anjo.chatter.tomp2p.parameters;

public class Parameters {

  private final String mode;
  private final String username;
  private final int listeningPort;
  private final int webSocketPort;
  private final int frontendPort;

  public Parameters(String[] args) {
    this.mode = getMode(args[0]);
    this.username = args[1];
    this.listeningPort = args.length >= 3 ? Integer.valueOf(args[2]) : 5000;

    int index = indexOfRendezvousString(args);
    this.webSocketPort = index == 4 ? Integer.valueOf(args[3]) : listeningPort + 3000;
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


  public int getListeningPort() {
    return listeningPort;
  }

  public int getWebSocketPort() {
    return webSocketPort;
  }

  public String getRendezvousString() {
    return "";
  }

  int indexOfRendezvousString(String[] args) {
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
