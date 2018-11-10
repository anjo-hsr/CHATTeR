package ch.anjo.chatter.main;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.tomp2p.parameters.ClientParameters;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.Validator;
import java.io.IOException;

public class ChatterServer {

  public static void main(String[] args) throws IOException {
    System.out.println("System startet on" + DateGenerator.getDate());

    if (!Validator.isArgsLengthOk(args)) {
      System.err.println(
          "Wrong parameters given. " + "Please use at mimimum: [mode] [username] [etherAddress]");
      System.err.println(args.length);
      terminate();
    }

    Parameters parameters = new Parameters(args);
    Validator validator = new Validator(parameters);

    if (!validator.areMinimalParametersGiven()) {
      terminate();
    }

    ChatterPeer myself = null;
    switch (parameters.getMode()) {
      case "master":
        myself = new ChatterPeer(parameters);
        break;
      case "client": {
        validator.setParameters(new ClientParameters(args));
        if (validator.areClientParametersCorrect()) {
          myself = new ChatterPeer((ClientParameters) validator.getParameters());
        } else {
          terminate();
        }
        break;
      }
      default:
        terminate();
    }

    startServices(parameters, myself);
  }

  private static void startServices(Parameters parameters, ChatterPeer myself) {
    Thread webSocketServer = new WebSocketService(parameters.getWebSocketPort());
    webSocketServer.start();

    TomP2pService.listen(myself, parameters.getWebSocketPort(), parameters.getUsername());
    webSocketServer.stop();
  }

  private static void terminate() {
    System.err.println(
        "\nUse the following parameters for the two modes:\n"
            + "\tmaster: \tmaster [username] [etherAddress] [listening port | default:5000] "
            + "[webSocket port | default:8000]\n"
            + "\tclient: \tclient [username] [etherAddress] [listening port | default:5000] "
            + "[webSocket port | default:8000] [username@ipAddress:port]");
    System.err.println("\n\nTerminating...");
    System.exit(1);
  }

  private static void logout() {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(() -> System.out.println("Adios"))
            // new Thread(me::disconnect)
        );
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
                    var user = readJson(exchange.getInputStream(), ChatterUser.class);
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
                    var user = readJson(exchange.getInputStream(), ChatterUser.class);
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