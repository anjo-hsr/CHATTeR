package ch.anjo.chatter.websocket.templates.chat;

import com.google.common.base.Objects;
import java.util.List;

public class ChatInformation {

  public String name;
  public List<String> peers;
  public boolean approved;

  public String getName() {
    return name;
  }

  public List<String> getPeers() {
    return peers;
  }

  public boolean isApproved() {
    return approved;
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
    return approved == that.approved &&
        Objects.equal(name, that.name) &&
        Objects.equal(peers, that.peers);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, peers, approved);
  }
}
