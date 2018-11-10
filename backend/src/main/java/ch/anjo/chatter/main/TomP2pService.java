package ch.anjo.chatter.main;

import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterWebSocketClient;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public class TomP2pService {

  public static void listen(ChatterPeer myself, int webSocketPort, String username) {
    try {
      // Wait till WebSocketServer in other thread was started.
      TimeUnit.SECONDS.sleep(10);

      ChatterWebSocketClient webSocketClient = new ChatterWebSocketClient(webSocketPort, username, myself);
      myself.replyToData(webSocketClient);

      webSocketClient.run();
    } catch (WebsocketNotConnectedException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }


}
