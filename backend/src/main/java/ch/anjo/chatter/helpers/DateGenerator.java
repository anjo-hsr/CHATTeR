package ch.anjo.chatter.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerator {

  public static String getDate() {
    DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return " ----- " + date.format(new Date()) + " ----- ";
  }
}
