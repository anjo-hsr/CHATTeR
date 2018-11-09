package ch.anjo.chatter.websocket;

public class ConnectionRequest {
  private final String myName;
  private final int myPort;
  private final String masterName;
  private final String masterIp;

  public ConnectionRequest(String myName, int myPort, String masterName, String masterIp) {
    this.myName = myName;
    this.myPort = myPort;
    this.masterName = masterName;
    this.masterIp = masterIp;
  }

  public String getMyName() {
    return myName;
  }

  public int getMyPort() {
    return myPort;
  }

  public String getMasterName() {
    return masterName;
  }

  public String getMasterIp() {
    return masterIp;
  }

  @Override
  public String toString() {
    return "ConnectionRequest{"
        + "myName='"
        + myName
        + '\''
        + ", myPort="
        + myPort
        + ", masterName='"
        + masterName
        + '\''
        + ", masterIp="
        + masterIp
        + '}';
  }
}
