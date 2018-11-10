package ch.anjo.chatter.tomp2p.helpers;

import ch.anjo.chatter.tomp2p.ChatterMessage;
import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.ChatterUser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Objects;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.storage.Data;

public class DataSender {

  private static BaseFutureAdapter<FutureSend> printConfirmation(ChatterPeer myself, String jsonMessage) {
    return new BaseFutureAdapter<FutureSend>() {
      @Override
      public void operationComplete(FutureSend future) throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject message = parser.parse(jsonMessage).getAsJsonObject();
        message.addProperty("received", true);
      }
    };
  }

  private static BaseFutureAdapter<FutureSend> nullConfirmation() {
    return new BaseFutureAdapter<FutureSend>() {
      @Override
      public void operationComplete(FutureSend futureSend) throws Exception {
      }
    };
  }

  public static void sendWithoutConfirmation(ChatterPeer myself, String receiver, String message) {
    sendWithListener(myself, receiver, message, nullConfirmation());
  }

  public static void sendWithConfirmation(ChatterPeer myself, String receiver, String message) {
    sendWithListener(myself, receiver, message, printConfirmation(myself, message));
  }


  private static void sendWithListener(ChatterPeer myself, String receiver, String message, BaseFutureAdapter listener) {
    ChatterUser chatterUser = myself.getChatterUser();
    chatterUser.getFriends()
        .stream()
        .filter(friend -> friend.equals(receiver))
        .map(friend -> myself.getPeerDht().get(chatterUser.getHash(friend)).start().awaitUninterruptibly().data())
        .filter(Objects::nonNull)
        .map(DataSender::readUser)
        .filter(Objects::nonNull)
        .forEach(
            friend -> {
              ChatterMessage sendingMessage = new ChatterMessage(chatterUser.getUsername(), friend.getUsername(), message);

              myself.getPeerDht().send(friend.getHash())
                  .object(sendingMessage)
                  .requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
                  .start()
                  .addListener(listener);
            });
  }

  private static ChatterUser readUser(Data data) {
    try {
      return (ChatterUser) data.object();
    } catch (Exception e) {
      return null;
    }
  }
}
