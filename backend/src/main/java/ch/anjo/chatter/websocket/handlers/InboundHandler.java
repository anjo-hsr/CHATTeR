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
      case MessageTypes.ADD_MESSAGE:
        {
          if (!handler
              .getMessages(webSocketMessage.chatId)
              .contains(webSocketMessage.messageInformation)) {
            saveMessage(handler, webSocketMessage.chatId, webSocketMessage.messageInformation);
            OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          }
          break;
        }
      case MessageTypes.CONFIRM_MESSAGE:
        {
          updateMessage(
              handler,
              webSocketMessage.chatId,
              webSocketMessage.messageId,
              webSocketMessage.username);
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          break;
        }
      case MessageTypes.ADD_CHAT:
      case MessageTypes.CHANGE_CHAT:
        {
          handler.getChatHandler().saveChat(webSocketMessage);
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          break;
        }
      case MessageTypes.DELETE_CHAT:
        {
          handler.getChatHandler().deleteChat(webSocketMessage.chatId);
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          break;
        }
      case MessageTypes.SELECT_CHAT:
        {
          OutboundHandler.sendChatMessages(handler, webSocketMessage.chatId);
          break;
        }
      case MessageTypes.GET_CHAT_PEERS:
        {
          OutboundHandler.sendChatPeers(
              handler, session, webSocketMessage.id, webSocketMessage.chatId);
          break;
        }
      case MessageTypes.ADD_PEERS:
      case MessageTypes.UPDATE_CHAT_PEERS:
        {
          OutboundHandler.sendPeers(
              handler, jsonMessage.replace(MessageTypes.UPDATE_CHAT_PEERS, MessageTypes.ADD_PEERS));
          break;
        }
      case MessageTypes.GET_PEERS:
        {
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          break;
        }
      case MessageTypes.APPROVE_CHAT:
        {
          OutboundHandler.sendMessageToSibling(handler, session, jsonMessage);
          break;
        }
      default:
    }
  }

  private static void saveMessage(Handler handler, String chatId, MessageInformation message) {
    handler.saveMessage(chatId, message);
  }

  private static void updateMessage(
      Handler handler, String chatId, String messageId, String signer) {
    handler.updateMessage(chatId, messageId, signer);
  }
}
