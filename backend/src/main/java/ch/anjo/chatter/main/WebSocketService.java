package ch.anjo.chatter.main;

import ch.anjo.chatter.websocket.handlers.InboundHandler;
import ch.anjo.chatter.websocket.handlers.OutboundHandler;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketService extends Thread {

  private final int webSocketPort;
  private final int frontendPort;
  private final Handler handler;
  private boolean browserStarted;

  public WebSocketService(int webSocketPort, int frontendPort) {
    this.webSocketPort = webSocketPort;
    this.frontendPort = frontendPort;
    this.handler = new Handler(null, "");
    this.browserStarted = false;
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
                    session.setIdleTimeout(600000);
                    String frontendUrl = "http://localhost:" + frontendPort;
                    if (!browserStarted && frontendPort == webSocketPort) {
                      try {
                        Desktop.getDesktop().browse(new URI(frontendUrl));
                        browserStarted = true;
                      } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                        getOpenBrowserMessage(frontendUrl);
                      }
                    } else if (handler.getSessionHandler().getBackendSession() == null) {
                      getOpenBrowserMessage(frontendUrl);
                    }

                    InboundHandler.handleSession(handler, session);
                    handler.getSessionHandler().printSession();

                    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
                    WsSession backendSession = handler.getSessionHandler().getBackendSession();

                    if (backendSession != null && backendSession.equals(session)) {
                      OutboundHandler.sendUsername(handler);
                    }

                    if (frontendSession != null && frontendSession.equals(session)) {
                      OutboundHandler.sendUsername(handler);
                      OutboundHandler.sendChats(handler);
                      OutboundHandler.sendMessageToSibling(
                          handler, session, OutboundHandler.createGetPeer());
                    }
                  });
              ws.onMessage(
                  (session, jsonMessage) -> {
                    Gson gson = new Gson();
                    System.out.println(jsonMessage);
                    WebSocketMessage webSocketMessage =
                        gson.fromJson(jsonMessage, WebSocketMessage.class);
                    InboundHandler.handleMessageTypes(
                        handler, session, jsonMessage, webSocketMessage);
                  });
            })
        .start(webSocketPort);
  }

  private void getOpenBrowserMessage(String frontendUrl) {
    System.out.println(
        " --- Please connect manually to the frontend via : " + frontendUrl + " ---");
  }
}
