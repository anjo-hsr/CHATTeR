package ch.anjo.chatter.tomp2p.helpers;

import static ch.anjo.chatter.tomp2p.ChatterPeer.readUser;

import ch.anjo.chatter.helpers.JsonGenerator;
import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.tomp2p.ChannelAction;
import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterUser;
import ch.anjo.chatter.tomp2p.TomP2pMessage;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.storage.Data;
import org.java_websocket.client.WebSocketClient;

public class DataReceiver {
  public static void replyToTomP2PData(ChatterPeer chatterPeer, WebSocketClient webSocketClient) {
    Peer myself = chatterPeer.getMyself();
    List<TomP2pMessage> messageHistory = chatterPeer.getMessageHistory();
    myself.objectDataReply(
        (sender, request) -> {
          TomP2pMessage tomP2pMessage = (TomP2pMessage) request;

          Gson gson = new Gson();
          WebSocketMessage webSocketMessage =
              gson.fromJson(tomP2pMessage.getJsonMessage(), WebSocketMessage.class);

          switch (webSocketMessage.type) {
            case MessageTypes.ADD_PEERS: {
              if (!tomP2pMessage.getSender().equals(chatterPeer.getChatterUser().getUsername())) {
                webSocketClient.send(tomP2pMessage.getJsonMessage());
              }
              break;
            }

            case MessageTypes.GET_PEERS: {
              if (!tomP2pMessage.getSender().equals(chatterPeer.getChatterUser().getUsername())) {
                String responseJson = ChannelAction.getFriends(chatterPeer);
                chatterPeer.addFriend(tomP2pMessage.getSender());
                return responseJson;
              }
              break;
            }
            case MessageTypes.ADD_CHAT:
            case MessageTypes.CHANGE_CHAT: {
              ChannelAction.sendPeerInformation(chatterPeer, webSocketMessage);
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
            case MessageTypes.CONFIRM_MESSAGE: {
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
            case MessageTypes.ADD_MESSAGE: {
              if (messageHistory
                  .stream()
                  .anyMatch(
                      message ->
                          webSocketMessage.messageInformation.author.equals(
                              chatterPeer.getChatterUser().getUsername())
                              || message.getJsonMessage().equals(tomP2pMessage.getJsonMessage()))) {
                return "";
              }
              messageHistory.add(tomP2pMessage);
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              confirmMessage(chatterPeer, webSocketMessage, tomP2pMessage);
              break;
            }
            default: {
              webSocketClient.send(tomP2pMessage.getJsonMessage());
              break;
            }
          }
          return "";
        });
  }






  private static void confirmMessage(
      ChatterPeer chatterPeer, WebSocketMessage webSocketMessage, TomP2pMessage tomP2pMessage) {
    if (!webSocketMessage.messageInformation.author.equals(
        chatterPeer.getChatterUser().getUsername())) {
      String response = JsonGenerator.generateConfirmMessage(chatterPeer, webSocketMessage);
      System.out.println(tomP2pMessage.getSender());
      DataSender.sendWithConfirmation(chatterPeer, tomP2pMessage.getSender(), response);
    }
  }

  private static String getFriends(ChatterPeer chatterPeer) {
    JsonObject responseJson = new JsonObject();
    responseJson.addProperty(MessageTypes.TYPE_KEYWORD, MessageTypes.ADD_PEERS);

    JsonArray peers = new JsonArray();
    Set<String> friends = chatterPeer.getChatterUser().getFriends();

    Set<JsonObject> peerSet = new HashSet<>();
    friends.forEach(
        friend ->
            chatterPeer
                .getDht()
                .get(ChatterUser.getHash(friend))
                .start()
                .addListener(
                    new BaseFutureAdapter<FutureGet>() {
                      @Override
                      public void operationComplete(FutureGet future) {
                        Data data = future.data();
                        if (!data.isEmpty()) {
                          ChatterUser friend = readUser(data);
                          if (Objects.nonNull(friend)) {
                            peerSet.add(friend.getInformation());
                          }
                        }
                      }
                    }));

    responseJson.add("peers", peers);

    return responseJson.toString();
  }
}
