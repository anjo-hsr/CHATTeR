package ch.anjo.chatter.http.services;

import ch.anjo.chatter.lib.ChatterPeer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class TomP2pService {

  public static void listen(ChatterPeer myself, int webSocketPort, String username) {
    try {
      myself.replyToData();

      WebSocketClient webSocketClient =
          new WebSocketClient(
              new URI(
                  "ws://localhost:" + webSocketPort + "/chat?wsType=backend&username=" + username)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
              //Do nothing in backend.
            }

            @Override
            public void onMessage(String message) {
              // Handle sending over TomP2P
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
      // Wait till WebSocketServer is started.
      TimeUnit.SECONDS.sleep(5);
      webSocketClient.connect();

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
