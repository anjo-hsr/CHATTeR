package ch.sbi.blockchat.lib;

import java.util.ArrayList;

public class CompleteHistory {

  private ArrayList<ChatHistory> chatHistories;

  public CompleteHistory() {
    chatHistories = new ArrayList<>();
  }

  public void addHistory(ChatHistory history) {
    chatHistories.add(history);
  }

  public ChatHistory getHistory(ClientPeer friend) {
    for (ChatHistory history : chatHistories) {
      if (history.getClientPeer().getUser().getEmail().equals(friend.getUser().getEmail())) {
        return history;
      }
    }
    return null;
  }
}
