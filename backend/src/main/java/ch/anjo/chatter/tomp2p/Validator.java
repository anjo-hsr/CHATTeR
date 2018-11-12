package ch.anjo.chatter.tomp2p;

import ch.anjo.chatter.tomp2p.parameters.Parameters;
import org.apache.commons.validator.routines.InetAddressValidator;

public class Validator {

  private Parameters parameters;

  public Validator(Parameters parameters) {
    this.parameters = parameters;
  }

  public void setParameters(Parameters parameters) {
    this.parameters = parameters;
  }

  public boolean areMinimalParametersGiven() {
    return areMasterParametersCorrect();
  }

  public boolean areMasterParametersCorrect() {
    return isUsernameCorrect()
        && isEtherAddressCorrect()
        && isPortValid(parameters.getListeningPort());
  }

  public boolean areClientParametersCorrect() {
    return areMasterParametersCorrect() && isRendezvousPointCorrect();
  }

  private boolean isUsernameCorrect() {
    return isUsernameCorrect(parameters.getUsername());
  }

  private boolean isUsernameCorrect(String username) {
    return parameters.getUsername().matches("[a-zA-Z0-9]{1,25}");
  }

  private boolean isEtherAddressCorrect() {
    return parameters.getEtherAddress().matches("0x[a-f0-9]{40}");
  }

  private boolean isRendezvousPointCorrect() {
    ChatterAddress rendezvousAddress;
    if ((rendezvousAddress = getRendezvousChatterAddress()) != null) {
      InetAddressValidator ipValidator = new InetAddressValidator();
      return isUsernameCorrect(rendezvousAddress.getUsername())
          && ipValidator.isValid(rendezvousAddress.getHost())
          && isPortValid(rendezvousAddress.getPort());
    }

    return false;
  }

  public ChatterAddress getRendezvousChatterAddress() {
    String rendezvousString = parameters.getRendezvousString();

    if (rendezvousString.contains(":") && rendezvousString.contains("@")) {
      String remoteUser = rendezvousString.split("@")[0];
      String hostAndPort = rendezvousString.split("@")[1];
      String host = hostAndPort.split(":")[0];
      int port = Integer.valueOf(hostAndPort.split(":")[1]);

      return new ChatterAddress(remoteUser, host, port);
    }
    return null;
  }

  private boolean isPortValid(int port) {
    // Minus 3000 because of the 3000 Port higher WebSocketService
    return port > 1024 && port < Math.pow(2, 32) - 1 - 3000;
  }

  public static boolean isArgsLengthOk(String[] args) {
    // The first three parameters are mandatory
    return args.length >= 3;
  }

  public Parameters getParameters() {
    return parameters;
  }
}
