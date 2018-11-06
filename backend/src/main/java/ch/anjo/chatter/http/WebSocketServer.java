package ch.anjo.chatter.http;

import ch.anjo.chatter.http.handlers.InboundHandler;
import ch.anjo.chatter.http.handlers.SessionHandler;
import ch.anjo.chatter.http.templates.Message;
import com.google.gson.Gson;
import io.javalin.Javalin;

public class WebSocketServer {

  private static final int PORT = 8000;

  public static void main(String[] args) {
    SessionHandler sessionHandler = new SessionHandler(null, "");
    WebSocketServer server = new WebSocketServer();
    runWebSocketServer(sessionHandler);
  }

  private static void runWebSocketServer(SessionHandler sessionHandler) {
    try {
      Javalin.create()
          .enableStaticFiles("/frontend")
          .enableCorsForOrigin("*")
          .ws(
              "/chat",
              ws -> {
                ws.onConnect(session -> {
                  InboundHandler.handleSession(sessionHandler, session);
                  sessionHandler.printSession();
                });
                ws.onMessage(
                    (session, JsonMessage) -> {
                      Gson gson = new Gson();
                      Message message = gson.fromJson(JsonMessage, Message.class);

                      InboundHandler.handleMessageTypes(sessionHandler, message);
                    });
              })
          .start(PORT);
    } catch (Exception e) {
      System.out.println("Exception");
      //Jap, empty catch block.
    }
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
