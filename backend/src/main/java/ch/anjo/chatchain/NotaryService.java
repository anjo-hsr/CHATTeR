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

  private final Web3j web3;
  private final Credentials credentials;
  private final CHATTeRNotaryContract notaryContract;

  public NotaryService(String wallet, String password) throws IOException, CipherException {
    web3 = Web3j.build(new HttpService(Constants.INFURA_ADDRESS));
    credentials =
        WalletUtils.loadCredentials(password, Constants.WALLET_PATH + wallet);
    notaryContract = CHATTeRNotaryContract
        .load(Constants.CONTRACT_ADDRESS, web3, credentials, new DefaultGasProvider());
  }

  public CompletableFuture<Void> storeMessage(String messageHash) {
    byte[] hash = Hash.sha256(messageHash.getBytes());

    return CompletableFuture.runAsync(
        () -> {
          try {
            notaryContract.storeMessage(hash).send();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  public boolean checkMessage(String messageHash, String senderWallet) {
    try {
      byte[] hash = Hash.sha256(messageHash.getBytes());
      String senderResponse = notaryContract.getSender(hash).send();
      return senderWallet.equals(senderResponse);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}