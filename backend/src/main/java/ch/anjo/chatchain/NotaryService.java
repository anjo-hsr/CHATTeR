package ch.anjo.chatchain;

import ch.anjo.chatchain.contract.CHATTeRNotaryContract;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class NotaryService {

  private final CHATTeRNotaryContract notaryContract;

  public NotaryService(String username) {
    Web3j web3 = Web3j.build(new HttpService(Constants.INFURA_ADDRESS));
    Credentials credentials;
    try {
      credentials = WalletUtils.loadCredentials(
          Constants.walletMap.get(username).getWallet().getPassword(),
          Constants.walletMap.get(username).getWallet().getWalletPath()
      );
    } catch (IOException | CipherException e) {
      e.printStackTrace();
      notaryContract = null;
      return;
    }
    notaryContract = CHATTeRNotaryContract
        .load(Constants.walletMap.get("contract").getAddress(), web3, credentials, new DefaultGasProvider());
    System.out.println(
        String.format("Wallet with address %s loaded", Constants.walletMap.get(username).getShortAddress())
    );

  }

  public void storeMessage(String messageHash) {
    byte[] hash = Hash.sha256(messageHash.getBytes());

    tryToStoreMessage(hash, 0);
  }

  private void tryToStoreMessage(byte[] hash, int counter) {
    int newCounter = counter + 1;
    waitIfRepeatedTry(counter);

    CompletableFuture.runAsync(
        () -> {
          try {
            notaryContract.storeMessage(hash).send();
            System.out.println("Message saved in smartContract.");
          } catch (Exception e) {
            if (counter <= 9) {
              tryToStoreMessage(hash, newCounter);
            } else {
              e.printStackTrace();
            }
          }
        }
    );
  }

  private void waitIfRepeatedTry(int counter) {
    if (counter > 1) {
      System.out.println(String.format("Try to store the message again in 10 Seconds.  #%s of 10)", counter));
      try {
        TimeUnit.SECONDS.sleep(10);
      } catch (InterruptedException e) {
        System.out.println("Problem with sleeping thread.");
      }
    }
  }

  public CompletableFuture<String> checkMessage(String messageHash) {
    byte[] hash = Hash.sha256(messageHash.getBytes());
    try {
      return notaryContract.getSender(hash).sendAsync();
    } catch (Exception e) {
      return null;
    }
  }
}
