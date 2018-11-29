package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import ch.anjo.chatter.websocket.templates.chat.ChatMessages;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatHandler {

  private HashMap<String, ChatInformation> chatStore;
  private HashMap<String, ChatMessages> chatMessages;

  ChatHandler() {
    this.chatStore = new HashMap<>();
    this.chatMessages = new HashMap<>();
  }

  public HashMap<String, ChatInformation> getChatStore() {
    return chatStore;
  }

  public List<String> getChatMessages(String chatId) {
    if (!chatMessages.containsKey(chatId)) {
      return new ArrayList<>();
    }
    ChatMessages messages = chatMessages.get(chatId);
    return messages.getMessagesAsString();
  }

  public ChatInformation getChatInformation(String chatId) {
    return chatStore.get(chatId);
  }

  public void saveChat(WebSocketMessage webSocketMessage) {
    chatStore.put(webSocketMessage.chatId, webSocketMessage.chatInformation);
  }

  public void deleteChat(String chatId) {
    chatStore.remove(chatId);
  }

  public boolean isMessageInHistory(WebSocketMessage webSocketMessage) {
    if (Objects.nonNull(webSocketMessage)) {
      ChatMessages possibleMessages = chatMessages.getOrDefault(webSocketMessage.chatId, new ChatMessages());
      return possibleMessages.contains(webSocketMessage.messageInformation.messageId);
    }
    return false;
  }

  void saveMessage(String chatId, MessageInformation message) {
    ChatMessages messages;
    if (!chatMessages.containsKey(chatId)) {
      messages = new ChatMessages();
    } else {
      messages = chatMessages.get(chatId);
    }
    messages.addMessage(message);
    chatMessages.put(chatId, messages);
  }

  boolean updateMessage(String chatId, String messageId, String signer) {
    if (chatMessages.containsKey(chatId)) {
      return chatMessages.get(chatId).updateMessage(messageId, signer);
    }
    return false;
  }
}
