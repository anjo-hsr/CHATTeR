package ch.anjo.chatter.main;

import ch.anjo.chatter.helpers.DateGenerator;
import ch.anjo.chatter.tomp2p.ChatterPeer;
import ch.anjo.chatter.tomp2p.Validator;
import ch.anjo.chatter.tomp2p.parameters.ClientParameters;
import ch.anjo.chatter.tomp2p.parameters.Parameters;
import java.io.IOException;
import java.util.Arrays;

public class ChatterServer {

  public static void main(String[] args) throws IOException {
    System.out.println(DateGenerator.getDate() + "System started");

    if (!Validator.isArgsLengthOk(args)) {
      System.err.println("Wrong parameters given.");
      System.err.println(Arrays.toString(args));
      terminate();
    }

    Parameters parameters = new Parameters(args);
    Validator validator = new Validator(parameters);

    if (!validator.areMinimalParametersGiven()) {
      terminate();
    }

    ChatterPeer chatterPeer = null;
    switch (parameters.getMode()) {
      case "master":
        chatterPeer = new ChatterPeer(parameters);
        break;
      case "client":
        {
          validator.setParameters(new ClientParameters(args));
          if (validator.areClientParametersCorrect()) {
            chatterPeer = new ChatterPeer((ClientParameters) validator.getParameters());
          } else {
            terminate();
          }
          break;
        }
      default:
        terminate();
    }

    startServices(parameters, chatterPeer);
  }

  private static void startServices(Parameters parameters, ChatterPeer myself) {
    Thread webSocketServer =
        new WebSocketService(parameters.getWebSocketPort(), parameters.getFrontendPort(), parameters.getUsername());
    webSocketServer.start();

    TomP2pServer tomP2pServer =
        new TomP2pServer(myself, parameters.getWebSocketPort(), parameters.getUsername());
    tomP2pServer.start();
  }

  private static void terminate() {
    System.err.println(
        "\nUse the following parameters for the two modes:\n"
            + "\tmaster: \tmaster[:dev] [username] [etherAddress] [listening port] "
            + "[webSocket port | default:listeningPort + 3000]\n"
            + "\tclient: \tclient[:dev] [username] [etherAddress] [listening port] "
            + "[username@ipAddress:port] [webSocket port | default:listeningPort + 3000]");
    System.err.println(
        "\nUse :dev if the frontend was loaded via react-scrip. This will prevent the opening of a new Browser Tab.");
    System.err.println("\n\nTerminating...");
    System.exit(1);
  }

  private static void newNumberArrived(ChatterPeer myself) {
    new Thread(myself::newNumberArrived).start();
  }
}
