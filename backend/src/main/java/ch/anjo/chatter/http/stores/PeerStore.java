package ch.anjo.chatter.http.stores;

import ch.anjo.chatter.http.templates.Peer;
import java.util.ArrayList;

public class PeerStore {

  private ArrayList<Peer> peers;

  public PeerStore(ArrayList<Peer> peers) {
    this.peers = peers;
  }

  public ArrayList<Peer> getPeers() {
    return peers;
  }

  public void addMessage(Peer peer) {
    peers.add(peer);
  }
}
