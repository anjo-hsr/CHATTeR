package ch.anjo.chatter.http.handlers.outbound;

import ch.anjo.chatter.http.handlers.SessionHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class OutboundHandler {

  public static void updateNewPeer(WsSession session) {
  }

  public static void sendPeers(SessionHandler sessionHandler, String receiver) {
    JsonObject message = new JsonObject();
    JsonArray sessions = new JsonArray();
    Map<String, WsSession> sessionMap = sessionHandler.getSessionNameSessionMap();
    sessionHandler
        .getKeySet()
        .forEach(
            username -> {
              WsSession wsSession = sessionMap.get(username);
              if (wsSession.isOpen()) {
                if (!wsSession.equals(sessionMap.get(receiver))) {
                  wsSession.send(message.toString());
                }
              }
            });
    message.addProperty("messageType", "sendPeers");
    message.add("peers", sessions);
    sendMessageToName(sessionHandler, receiver, message.toString());
  }

  public static void sendMessageToName(
      SessionHandler sessionHandler, String receiver, String messageString) {
    sessionHandler.getSession(receiver).send(messageString);
  }

  public static void sendMessageToSession(WsSession receiver, String messageString) {
    receiver.send(messageString);
  }

  public static void broadcastPeers(SessionHandler sessionHandler, WsSession receiver) {
    JsonArray peers = new JsonArray();
    ArrayList<String> peerKeys = new ArrayList<>(sessionHandler.getKeySet());
    peerKeys.forEach(peers::add);

    JsonObject message = new JsonObject();
    message.addProperty("type", "ADD_PEERS");
    message.addProperty("peers", peers.getAsString());

    sendMessageToSession(receiver, message.toString());
  }

  public static void broadcastPeers(SessionHandler sessionHandler) {
    ArrayList<String> peers = new ArrayList<>(sessionHandler.getKeySet());

    sessionHandler.getKeySet()
        .forEach(key -> sendMessageToSession(
            sessionHandler.getSession(key),
            Arrays.toString(peers.toArray())
            )
        );
  }

  public static void broadcastMessage(
      SessionHandler sessionHandler, String sender, String messageString) {
    JsonObject message = new JsonObject();
    message.addProperty("messageType", "broadcast");
    message.addProperty("sender", sender);
    message.addProperty("message", messageString);

    sessionHandler
        .getKeySet()
        .forEach(
            username -> {
              WsSession wsSession = sessionHandler.getSessionNameSessionMap().get(username);
              if (wsSession.isOpen()) {
                wsSession.send(message.toString());
              }
            });
  }
}
