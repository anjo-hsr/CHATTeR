package ch.anjo.chatchain;

import ch.anjo.chatchain.contract.CHATTeRNotaryContract;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
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

    CompletableFuture.runAsync(
        () -> {
          try {
            notaryContract.storeMessage(hash).send();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    );
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