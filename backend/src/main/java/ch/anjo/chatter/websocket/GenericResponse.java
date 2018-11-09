package ch.anjo.chatter.websocket;

public class GenericResponse {
  private final String message;

  public GenericResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
