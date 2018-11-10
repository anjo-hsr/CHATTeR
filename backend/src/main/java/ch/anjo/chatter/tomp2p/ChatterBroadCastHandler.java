package ch.anjo.chatter.tomp2p;

import java.util.concurrent.atomic.AtomicInteger;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.StructuredBroadcastHandler;

public class ChatterBroadCastHandler extends StructuredBroadcastHandler {
  public ChatterBroadCastHandler() {
  }

  @Override
  public StructuredBroadcastHandler receive(Message message) {
    System.out.println("received message " + message.sender());

    return super.receive(message);
  }

  @Override
  public StructuredBroadcastHandler init(Peer peer) {
    return super.init(peer);
  }
};
