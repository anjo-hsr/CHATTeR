package ch.anjo.chatter.websocket.handlers;

import ch.anjo.chatter.helpers.JsonGenerator;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import io.javalin.websocket.WsSession;
import java.util.List;
import java.util.Objects;

public class OutboundHandler {

  public static void sendChats(Handler handler) {
    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
    if (Objects.isNull(frontendSession)) {
      return;
    }

    String message = JsonGenerator.generateAddChats(handler);
    frontendSession.send(message);
  }


  public static void sendUsername(Handler handler) {
    WsSession frontendSession = handler.getSessionHandler().getFrontendSession();
    if (Objects.isNull(frontendSession) || !frontendSession.isOpen()) {
      return;
    }

    String message = JsonGenerator.generateSetUsername(handler);

    frontendSession.send(message);
  }

  public static void sendMessageToSibling(
      Handler handler, WsSession session, String messageString) {
    if (handler.getSessionHandler().existsSessionSibling()) {
      WsSession sessionSibling = handler.getSessionHandler().getSessionSibling(session);
      sessionSibling.send(messageString);
    } else {
      System.err.println("No sibling found! - Please check the WebSocket connections");
    }
  }

  static void sendChatMessages(Handler handler, String chatId) {
    WsSession session = handler.getSessionHandler().getFrontendSession();
    List<String> messages = handler.getChatHandler().getChatMessages(chatId);

    String response = JsonGenerator.generateAddMessages(chatId, messages);
    session.send(response);
  }


  static void sendChatPeers(Handler handler, WsSession session, String id, String chatId) {
    List<String> peerList = handler.getChatHandler().getChatInformation(chatId).getPeers();

    String peerMessage = JsonGenerator.generateSendChatPeers(id, chatId, peerList);

    session.send(peerMessage);
  }
}
