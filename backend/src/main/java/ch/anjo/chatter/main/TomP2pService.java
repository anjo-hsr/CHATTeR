package ch.anjo.chatter.main;

import ch.anjo.chatter.tomp2p.ChatterPeer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class TomP2pService {

  public static void listen(ChatterPeer myself, int webSocketPort, String username) {
    try {
      // Wait till WebSocketServer in other thread was started.
      TimeUnit.SECONDS.sleep(10);

      WebSocketClient webSocketClient =
          new WebSocketClient(
              new URI(
                  "ws://localhost:" + webSocketPort + "/chat?wsType=backend&username=" + username)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
              System.out.println("Connected with WebSocket.");
            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
              System.out.println("closed connection");
            }

            @Override
            public void onError(Exception ex) {
              ex.printStackTrace();
            }
          };
      webSocketClient.connect();

      myself.replyToData(webSocketClient);

      while (true) {
        // Wait for messages
        webSocketClient.send("");
        TimeUnit.SECONDS.sleep(1);
      }
    } catch (InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
