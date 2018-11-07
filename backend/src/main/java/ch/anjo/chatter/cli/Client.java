package ch.sbi.blockchat.cli;

import ch.sbi.blockchat.lib.ClientPeer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import org.web3j.crypto.CipherException;

public class Client {
  public static void main(String[] args)
      throws IOException, ClassNotFoundException, CipherException {
    if (args.length <= 0) {
      System.out.println("Not all required command line arguments passed. Exiting.");
      return;
    }
    switch (args[0]) {
      case "setup":
        System.out.println("Starting client in listen mode as master on port 4000.");
        ClientPeer master = new ClientPeer(args[1], args[2], 4000);
        addExitHook(master);
        listen(master);
        break;
      case "connect":
        if (args[1] == null || args[2] == null) {
          throw new RuntimeException("Must provide peer IP address and peer port");
        }
        ClientPeer client =
            new ClientPeer(args[1], args[2], args[3], args[4], Integer.valueOf(args[5]));
        System.out.println(String.format("Connecting to %s", args[1]));
        addExitHook(client);
        listen(client);
        break;
      default:
        System.out.println("Invalid client mode passed. Exiting.");
    }
  }

  private static void addExitHook(final ClientPeer me) {
    Runtime.getRuntime().addShutdownHook(new Thread(me::disconnect));
  }

  private static void listen(ClientPeer me) throws IOException, ClassNotFoundException {
    me.addDataReply();
    for (Scanner scanner = new Scanner(System.in); ; ) {
      var message = scanner.nextLine();
      if (message.startsWith("$")) {
        var command = message.replace("$", "").split(" ");
        switch (command[0]) {
          case ("add"):
            me.addPeer(command[1]);
            break;
          case ("send"):
            me.send(command[1], String.join(" ", Arrays.copyOfRange(command, 2, command.length)));
            break;
          case ("broadcast"):
            me.sendAll(message);
            break;
          default:
            System.out.println("Invalid command.");
            break;
        }
      } else {
        System.out.println("Invalid command.");
      }
    }
  }
}
