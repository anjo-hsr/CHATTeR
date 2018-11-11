package ch.anjo.chatter.websocket.handlers.handlerClasses;

import ch.anjo.chatter.websocket.templates.WebSocketMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LoopHandler {

  private Map<WebSocketMessage, Date> lastMessages;

  public LoopHandler() {
    this.lastMessages = new HashMap<>();
  }

  public Map<WebSocketMessage, Date> getLastMessages() {
    return lastMessages;
  }

  public void setNewMessage(WebSocketMessage webSocketMessage) {
    lastMessages.put(webSocketMessage, new Date());
  }

  public boolean isLoopMessage(WebSocketMessage webSocketMessage) {
    boolean didALoopOccurred = lastMessages.keySet().stream().anyMatch(webSocketMessage::equals);
    cleanUpMessages();
    return didALoopOccurred;
  }


  private void cleanUpMessages() {
    Date currentDate = new Date();

    Set<WebSocketMessage> deleteSet = lastMessages.keySet().stream().filter(webSocketMessage -> {
      Date messageDate = lastMessages.get(webSocketMessage);
      return currentDate.getMinutes() - messageDate.getMinutes() > 2;
    }).collect(Collectors.toSet());

    deleteSet.forEach(lastMessages::remove);
  }
}
