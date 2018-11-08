package ch.anjo.chatter.http.stores;

import ch.anjo.chatter.http.templates.Peer;
import java.util.ArrayList;
import java.util.List;

public class PeerStore {

  private List<Peer> peers;

  public PeerStore(ArrayList<Peer> peers) {
    this.peers = peers;
  }

  public List<Peer> getPeers() {
    return peers;
  }

  public void addMessage(Peer peer) {
    peers.add(peer);
  }
}
