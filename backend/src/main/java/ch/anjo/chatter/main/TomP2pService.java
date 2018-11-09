package ch.anjo.chatter.main;

import ch.anjo.chatter.tomp2p.ChatterPeer;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
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
              System.out.println("Closed connection:\n"+reason);
            }

            @Override
            public void onError(Exception ex) {
              ex.printStackTrace();
            }
          };

      webSocketClient.connect();
      TimeUnit.SECONDS.sleep(2);
      myself.replyToData(webSocketClient);

      JsonObject message = new JsonObject();
      message.addProperty("type", "PING_MESSAGE");

      while (true) {
        // Wait for messages
        webSocketClient.send(message.toString());
        TimeUnit.SECONDS.sleep(5);
      }
    } catch ( WebsocketNotConnectedException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
