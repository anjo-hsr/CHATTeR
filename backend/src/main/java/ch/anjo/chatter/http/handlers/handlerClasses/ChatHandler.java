package ch.anjo.chatter.http.handlers.handlerClasses;

import ch.anjo.chatter.http.templates.Message;
import ch.anjo.chatter.http.templates.chat.ChatInformation;
import ch.anjo.chatter.http.templates.chat.ChatMessages;
import java.util.ArrayList;
import java.util.HashMap;

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

  public ArrayList<String> getChatMessages(String chatId) {
    if (!chatMessages.containsKey(chatId)) {
      return new ArrayList<>();
    }
    ChatMessages messages = chatMessages.get(chatId);
    return messages.getMessages();
  }

  public ChatInformation getChatInformation(String chatId) {
    return chatStore.get(chatId);
  }

  public void putChatInformation(String chatId, ChatInformation chatInformation) {
    chatStore.put(chatId, chatInformation);
  }

  public void saveChat(Message message) {
    chatStore.put(message.chatId, message.chatInformation);
  }

  public void deleteChat(String chatId){
    chatStore.remove(chatId);
  }

  public void saveMessage(String chatId, String message) {
    ChatMessages messages;
    if (!chatMessages.containsKey(chatId)) {
      messages = new ChatMessages();
    } else {
      messages = chatMessages.get(chatId);
    }
    messages.addMessage(message);
    chatMessages.put(chatId, messages);
  }
}
