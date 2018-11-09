package ch.anjo.chatter.http.services;

import ch.anjo.chatter.http.handlers.InboundHandler;
import ch.anjo.chatter.http.handlers.OutboundHandler;
import ch.anjo.chatter.http.handlers.handlerClasses.Handler;
import ch.anjo.chatter.http.templates.Message;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;

public class WebSocketService extends Thread {

  private final int port;
  private final Handler handler;

  public WebSocketService(int webSocketPort) {
    this.port = webSocketPort;
    this.handler = new Handler(null, "");
  }

  public void run() {
    Javalin.create()
        .enableStaticFiles("/frontend")
        .enableCorsForOrigin("*")
        .ws(
            "/chat",
            ws -> {
              ws.onConnect(
                  session -> {
                    InboundHandler.handleSession(handler, session);
                    handler.getSessionHandler().printSession();

                    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
                    if (frontendSession != null && frontendSession.equals(session)) {
                      OutboundHandler.sendUsername(handler);
                      OutboundHandler.sendPeers(handler);
                      OutboundHandler.sendChats(handler);
                    }
                  });
              ws.onMessage(
                  (session, jsonMessage) -> {
                    Gson gson = new Gson();
                    System.out.println(jsonMessage);
                    Message message = gson.fromJson(jsonMessage, Message.class);

                    InboundHandler.handleMessageTypes(handler, session, jsonMessage, message);
                  });
            })
        .start(port);
  }
}