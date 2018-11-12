package ch.anjo.chatter.tomp2p.parameters;


public class ClientParameters extends Parameters {

  private final String rendezvousString;

  public ClientParameters(String[] args) {
    super(args);

    int index = super.indexOfRendezvousString(args);

    this.rendezvousString = index != -1 ? args[index] : "";
  }

  @Override
  public String getRendezvousString() {
    return rendezvousString;
  }
}
