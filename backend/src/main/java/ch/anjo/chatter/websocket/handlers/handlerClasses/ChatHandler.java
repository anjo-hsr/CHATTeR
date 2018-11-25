package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import ch.anjo.chatter.websocket.templates.chat.ChatInformation;
import ch.anjo.chatter.websocket.templates.chat.ChatMessages;
import ch.anjo.chatter.websocket.templates.message.MessageInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatHandler {

  private HashMap<String, ChatInformation> chatStore;
  private HashMap<String, ChatMessages> chatMessages;

  public ChatHandler() {
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

  public void saveMessage(String chatId, MessageInformation message) {
    ChatMessages messages;
    if (!chatMessages.containsKey(chatId)) {
      messages = new ChatMessages();
    } else {
      messages = chatMessages.get(chatId);
    }
    messages.addMessage(message);
    chatMessages.put(chatId, messages);
  }

  public void updateMessage(String chatId, String messageId, String signer){
    if (chatMessages.containsKey(chatId)) {
      chatMessages.get(chatId).updateMessage(messageId, signer);
    }
  }
}
