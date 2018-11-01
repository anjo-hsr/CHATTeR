package ch.anjo.chatter.http;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import ch.anjo.chatter.lib.HttpPeer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.util.Headers;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Api {
  private static <T> T readJson(InputStream stream, Class<T> type) {
    JsonReader reader = new JsonReader(new InputStreamReader(stream));
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(reader, type);
  }

  private static <T> T readJson(String string, Class<T> type) {
    return new GsonBuilder().create().fromJson(string, type);
  }

  private static <T> String toJsonString(T payload) {
    return new GsonBuilder().create().toJson(payload);
  }

  // this is really, really, really ugly, don't do this at home. Please don't :(
  // In reality, this would have to use something like gRPC and completely communicate
  // using ONLY websockets. Designed properly, that wouldn't even need a backend most
  // likely.
  public static void main(String[] args) throws IOException {

    ClassPathResourceManager manager = new ClassPathResourceManager(Api.class.getClassLoader());
    var sessions = new HashMap<String, HttpPeer>();

    // this is the client control channel over http. Instead of blocking handlers, these
    // should all have proper callbacks that are passed into httppeer and peppered with
    // real information there.
    RoutingHandler paths =
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

    server.start();
  }
}
