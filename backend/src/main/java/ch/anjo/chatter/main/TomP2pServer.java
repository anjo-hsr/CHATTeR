package ch.anjo.chatter.main;

import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterWebSocketClient;
import java.util.concurrent.TimeUnit;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public class TomP2pServer {

  private final ChatterPeer myself;
  private final int webSocketPort;
  private final String username;

  TomP2pServer(ChatterPeer myself, int webSocketPort, String username) {
    this.myself = myself;
    this.webSocketPort = webSocketPort;
    this.username = username;
  }

  public void start() {
    try {
      // Wait till WebSocketServer in other thread was started.
      TimeUnit.SECONDS.sleep(10);

      ChatterWebSocketClient.generateChatterWebSocketClient(webSocketPort, username, myself);
    } catch (WebsocketNotConnectedException | InterruptedException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
