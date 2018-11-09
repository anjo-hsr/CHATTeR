package ch.anjo.chatter.tomp2p.parameters;

public class ClientParameters extends Parameters {

  private final String rendezvousString;

  public ClientParameters(String[] args) {
    super(args);

    this.rendezvousString = args.length >= 6 ? args[5] : "";
  }

  @Override
  public String getRendezvousString() {
    return rendezvousString;
  }
}
