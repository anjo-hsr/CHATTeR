package ch.anjo.chatter.websocket.handlers;

import ch.anjo.chatter.helpers.MessageTypes;
import ch.anjo.chatter.websocket.handlers.handlerClasses.Handler;
import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import io.javalin.websocket.WsSession;

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
      case MessageTypes.CHECK_SENDER:{
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
      }
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT: {
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
        break;
      }
      case MessageTypes.GET_CHAT_PEERS: {
        OutboundHandler.sendChatPeers(
            handler, session, webSocketMessage.id, webSocketMessage.chatId);
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
      case MessageTypes.APPROVE_CHAT: {
        OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
        break;
      }
      default:
    }
  }

  private static void saveMessage(Handler handler, String chatId, MessageInformation message) {
    handler.saveMessage(chatId, message);
  }

  private static boolean updateMessage(
      Handler handler, String chatId, String messageId, String signer) {
    return handler.updateMessage(chatId, messageId, signer);
  }
}
