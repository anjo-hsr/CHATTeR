package ch.anjo.chatter.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer {

  private static final int PORT = 8000;
  private static int sessionCounter = 1;
  private static Map<WsSession, String> sessionUsernameMap = new ConcurrentHashMap<>();


  public static void main(String[] args) {
    runWebSocketServer();
  }

  private static void runWebSocketServer() {
    Javalin.create()
        .enableStaticFiles("/frontend")
        .enableCorsForOrigin("*")
        .ws(
            "/chat",
            ws -> {
              ws.onConnect(
                  session -> {
                    String username = "UserID" + sessionCounter++;
                    sessionUsernameMap.put(session, username);
                    broadcastPeers();
                  });
              ws.onMessage(
                  (session, JsonMessage) -> {
                    Gson gson = new Gson();
                    Message message = gson.fromJson(JsonMessage, Message.class);

                    switch (message.messageType) {
                      case "chat":
                        broadcastMessage(sessionUsernameMap.get(session), message.message);
                        break;
                      case "getPeers":
                        sendPeers(session);
                        break;
                      case "setName":
                        sessionUsernameMap.replace(session, message.message);
                        broadcastPeers();
                        System.out.println(sessionUsernameMap.get(session) + " joined CHATTeR");
                        break;
                    }
                  });
            })
        .start(PORT);
  }

  private static void sendPeers(WsSession reciever) {
    JsonObject message = new JsonObject();
    JsonArray sessions = new JsonArray();
    sessionUsernameMap
        .keySet()
        .stream()
        .filter(WsSession::isOpen)
        .forEach(session -> {
          if (!session.equals(reciever)) {
            sessions.add(sessionUsernameMap.get(session));
          }
        });
    message.addProperty("messageType", "sendPeers");
    message.add("peers", sessions);
    sendMessage("Server", reciever, message.toString());
  }

  private static void sendMessage(String sender, WsSession reciever, String messageString) {
    reciever.send(messageString);
  }

  private static void broadcastPeers() {
    for (WsSession reciever : sessionUsernameMap.keySet()) {
      sendPeers(reciever);
    }
  }

  private static void broadcastMessage(String sender, String messageString) {
    JsonObject message = new JsonObject();
    message.addProperty("messageType", "broadcast");
    message.addProperty("sender", sender);
    message.addProperty("message", messageString);

    sessionUsernameMap
        .keySet()
        .stream()
        .filter(WsSession::isOpen)
        .forEach(
            session -> {
              session.send(message.toString());
            });
  }

  // this is the client control channel over http. Instead of blocking handlers, these
  // should all have proper callbacks that are passed into httppeer and peppered with
  // real information there.

  /*RoutingHandler paths =
      new RoutingHandler()
          .post(
              "/start",
              new BlockingHandler(
                  exchange -> {
                    exchange.startBlocking();
                    var user = readJson(exchange.getInputStream(), WebUser.class);
                    var peer = new HttpPeer(user);
                    sessions.put(user.getName(), peer);
                    var response = new GenericResponse("Started new DHT");
                    exchange.getResponseSender().send(toJsonString(response));
                    exchange.endExchange();
                  }))
          .post(
              "/join",
              new BlockingHandler(
                  exchange -> {
                    exchange.startBlocking();
                    exchange.getRequestHeaders().add(Headers.CONTENT_TYPE, "application/json");
                    var connection = readJson(exchange.getInputStream(), ConnectionRequest.class);
                    var peer = new HttpPeer(connection);
                    sessions.put(connection.getMyName(), peer);
                    var response =
                        new GenericResponse(
                            String.format(
                                "Joined DHT by connecting to %s", connection.getMasterName()));
                    exchange.getResponseSender().send(toJsonString(response));
                    exchange.endExchange();
                  }))
          .post(
              "/disconnect",
              new BlockingHandler(
                  exchange -> {
                    exchange.startBlocking();
                    var user = readJson(exchange.getInputStream(), WebUser.class);
                    String format = String.format("Disconnecting user %s", user);
                    System.out.println(format);

                    exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send(format);
                    exchange.endExchange();
                  }))
          .post(
              "/friend",
              new BlockingHandler(
                  exchange -> {
                    exchange.startBlocking();
                    var friendship = readJson(exchange.getInputStream(), FriendRequest.class);
                    sessions.get(friendship.getMe()).addPeer(friendship.getOther());
                    var response =
                        new GenericResponse(
                            String.format("Added %s to friend list", friendship.getOther()));
                    exchange.getResponseSender().send(toJsonString(response));
                    exchange.endExchange();
                  }));

  // this is the chat channel over websockets, this just sends messages and the entry page
  PathHandler handler =
      path(paths)
          .addPrefixPath(
              "/chat",
              websocket(
                  (exchange, channel) -> {
                    // to send data to the client, you can just send stuff on the channel
                    WebSockets.sendText(
                        String.format(
                            "Connected to chat backend on %s",
                            channel.getSourceAddress().toString()),
                        channel,
                        null);

                    channel
                        .getReceiveSetter()
                        .set(
                            new AbstractReceiveListener() {
                              @Override
                              protected void onFullTextMessage(
                                  WebSocketChannel channel, BufferedTextMessage message) {
                                try {
                                  var webMessage = readJson(message.getData(), WebMessage.class);
                                  var me = sessions.get(webMessage.getFrom());
                                  // yeah yeah, this should only happen once when creating
                                  // the client socket connection. This is just the ugly
                                  // workaround.
                                  if (webMessage.getContent().length() <= 0) {
                                    me.addDataReply(channel);
                                  } else {
                                    me.sendAll(webMessage.getContent());
                                    var self = String.format("Me: %s", webMessage.getContent());
                                    WebSockets.sendText(self, channel, null);
                                  }
                                } catch (Exception e) {
                                  // yep, that's fucking right bitch.
                                  // deal with it.
                                }
                              }
                            });
                    channel.resumeReceives();
                  }))
          .addExactPath("/", resource(manager).addWelcomeFiles("index.html"));

  Undertow server =
      Undertow.builder().addHttpListener(8000, "localhost").setHandler(handler).build();

  server.start();*/
}
