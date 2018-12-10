package ch.anjo.chatter.websocket.templates.chat;

import com.google.common.base.Objects;
import java.util.List;

public class ChatInformation {

  public String name;
  public List<String> peers;
  public List<String> oldPeers;

  public String getName() {
    return name;
  }

  public List<String> getPeers() {
    return peers;
  }

  public List<String> getOldPeers() {
    return oldPeers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatInformation that = (ChatInformation) o;
    return Objects.equal(name, that.name) && Objects.equal(peers, that.peers);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, peers);
  }
}
