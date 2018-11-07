package ch.sbi.blockchat.lib;

import java.util.Date;

public class Message {
  private Long id;
  private String text;
  private ClientPeer autor;
  private Date time;

  public Message() {}

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public ClientPeer getAutor() {
    return autor;
  }

  public void setAutor(ClientPeer autor) {
    this.autor = autor;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return autor.getUser() + "\n\t" + text;
  }
}
