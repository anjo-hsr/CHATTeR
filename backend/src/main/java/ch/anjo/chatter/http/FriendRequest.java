package ch.anjo.chatter.http;

public class FriendRequest {
  private final String me;
  private final String other;

  public FriendRequest(String me, String other) {
    this.me = me;
    this.other = other;
  }

  public String getMe() {
    return me;
  }

  public String getOther() {
    return other;
  }

  @Override
  public String toString() {
    return "FriendRequest{" + "me='" + me + '\'' + ", other='" + other + '\'' + '}';
  }
}
