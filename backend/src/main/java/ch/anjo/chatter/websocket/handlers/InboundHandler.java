package ch.anjo.chatter.websocket.handlers;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import com.google.gson.JsonArray;
import io.javalin.websocket.WsSession;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class InboundHandler {

  public static void handleSession(Handler handler, WsSession session) {
    String wsType = session.queryParam("wsType");
    if (wsType != null) {
      handler.getSessionHandler().replaceSession(session, wsType);
    }
  }

  public static void handleMessageTypes(
      Handler handler, WsSession session, String jsonMessage, WebSocketMessage webSocketMessage) {
    switch (webSocketMessage.type) {
      case MessageTypes.ADD_MESSAGE: {
        if (!handler.getChatHandler().isMessageInHistory(webSocketMessage)) {
          saveMessage(handler, webSocketMessage.chatId, webSocketMessage.messageInformation);
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        }
        break;
      }
      case MessageTypes.CONFIRM_MESSAGE: {
        boolean didAMessageGetUpdated = updateMessage(
            handler,
            webSocketMessage.chatId,
            webSocketMessage.messageId,
            webSocketMessage.username);
        if (didAMessageGetUpdated) {
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        }
        break;
      }
      case MessageTypes.CHECK_SIGNATURE: {
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      case MessageTypes.RESPONSE_CHECK_SIGNATURE: {
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT: {
        if (handler.getSessionHandler().getFrontendSession().equals(session) && Objects
            .nonNull(handler.getChatHandler().getChatInformation(webSocketMessage.chatId))) {
          List<String> oldChatPeers = handler.getChatHandler().getChatInformation(webSocketMessage.chatId).getPeers();
          jsonMessage = injectOldPeers(jsonMessage, oldChatPeers);
        }
        handler.getChatHandler().saveChat(webSocketMessage);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      case MessageTypes.LEAVE_CHAT: {
        handler.getChatHandler().deleteChat(webSocketMessage.chatId);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage.replace(
            MessageTypes.LEAVE_CHAT, MessageTypes.CHANGE_CHAT
        ));
        break;
      }
      case MessageTypes.SELECT_CHAT: {
        OutboundHandler.sendChatMessages(handler, webSocketMessage.chatId);
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      case MessageTypes.GET_CHAT_PEERS: {
        OutboundHandler.sendChatPeers(
            handler, session, webSocketMessage.waitingMessageId, webSocketMessage.chatId);
        break;
      }
      case MessageTypes.ADD_PEERS:
      case MessageTypes.UPDATE_CHAT_PEERS: {
        OutboundHandler.sendMessageToSibling(
            handler, session, jsonMessage.replace(MessageTypes.UPDATE_CHAT_PEERS, MessageTypes.ADD_PEERS));
        break;
      }
      case MessageTypes.GET_PEERS: {
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      default:
    }
  }

  @NotNull
  private static String injectOldPeers(String jsonMessage, List<String> oldChatPeers) {
    JsonArray oldPeerArray = new JsonArray();
    oldChatPeers.forEach(oldPeerArray::add);
    String searchString = ",\"peers\":";
    String injectionString = ",\"oldPeers\":" + oldPeerArray.toString() + searchString;
    return jsonMessage.replace(searchString, injectionString);
  }

  private static void saveMessage(Handler handler, String chatId, MessageInformation message) {
    handler.saveMessage(chatId, message);
  }

  private static boolean updateMessage(
      Handler handler, String chatId, String messageId, String signer) {
    return handler.updateMessage(chatId, messageId, signer);
  }
}
