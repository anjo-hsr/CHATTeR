package ch.anjo.chatter.http.templates;

public class ConnectionObject {

  public String name;
  public String masterIpAddress;
  public int masterPortNumber;

  public void printInformation() {
    System.out.println(
        "User with name: " + name + " is connected to " + masterIpAddress + ":" + masterPortNumber);
  }
}
