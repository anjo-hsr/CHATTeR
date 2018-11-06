package ch.anjo.chatter.http.handlers;

import ch.anjo.chatter.http.templates.Peer;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.common.base.Charsets;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class OutboundHandler {

  public OutboundHandler() {
  }

  public static void sendPeers(SessionHandler sessionHandler) {
    JsonObject message = new JsonObject();
    JsonArray sessions = new JsonArray();
    message.addProperty("messageType", "sendPeers");
    message.add("peers", sessions);
    sendMessage(sessionHandler, message.toString());
  }

  public static void broadcastPeers(SessionHandler sessionHandler) {
    Peer[] peers = getPeerArray();
    JsonArray jsonPeers = new JsonArray();


    Arrays.stream(peers).forEach(peer -> {
          jsonPeers.add(peer.username);
        });

    JsonObject message = new JsonObject();
    message.addProperty("type", "ADD_PEERS");
    message.addProperty("peers", jsonPeers.toString());

    sendMessage(sessionHandler, message.toString());
  }

  public static void broadcastMessage(
    SessionHandler sessionHandler, String sender, String messageString) {
    JsonObject message = new JsonObject();
    message.addProperty("type", "MESSAGE_BROADCAST");
    message.addProperty("sender", sender);
    message.addProperty("message", messageString);
    sendMessage(sessionHandler, message.toString());
  }

  private static Peer[] getPeerArray(){
    OutboundHandler outboundHandler = new OutboundHandler();
    String jsonString = null;
    try {
      jsonString = outboundHandler.readResource("peers/peers.json", Charsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Gson gson = new Gson();
    return gson.fromJson(jsonString, Peer[].class);
  }

  private static void sendMessage(SessionHandler sessionHandler, String messageString) {
    sessionHandler.getSession().send(messageString);
  }

  public String readResource(final String fileName, Charset charset) throws IOException {
    return Resources.toString(Resources.getResource(fileName), charset);
  }
}
