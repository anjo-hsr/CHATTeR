package ch.anjo.chatter.main;

import ch.anjo.chatter.websocket.handlers.InboundHandler;
import ch.anjo.chatter.websocket.handlers.OutboundHandler;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.Message;
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
                    Message message = gson.fromJson(jsonMessage, Message.class);
                    if(message != null && !message.type.equals("PING_MESSAGE")){
                      System.out.println(message);
                    }
                    InboundHandler.handleMessageTypes(handler, session, jsonMessage, message);
                  });
            })
        .start(port);
  }
}