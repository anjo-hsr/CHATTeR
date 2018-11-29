package ch.anjo.chatter.tomp2p;

import ch.anjo.chatchain.Constants;
import ch.anjo.chatchain.infura.InfuraSettings;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class NotaryService {

  private final Web3j web3j;
  private final Credentials credentials;
  // private final Notary notary;

  public NotaryService(String walletName, String walletPassword)
      throws IOException, CipherException {
    this.web3j = Web3j.build(new HttpService(InfuraSettings.INFURA_ADDRESS));
    this.credentials =
        WalletUtils.loadCredentials(walletPassword, Constants.WALLET_PATH + walletName);
    // this.notary = Notary.load()
  }

  public CompletableFuture<Void> storeMessageOnBlockchain(String originalMessage) {
    var hashString = Hash.sha256(originalMessage.getBytes());

    return CompletableFuture.runAsync(
        () -> {
          // notary.store(hash).send();
        });
  }

  public String getHexAddress() {
    return credentials.getAddress();
  }

  public CompletableFuture<Boolean> isValid() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return isAValidWeb3Client(web3j).get(); // && notary.isValid();
          } catch (InterruptedException | ExecutionException e) {
            return false;
          }
        });
  }

  private CompletableFuture<Boolean> isAValidWeb3Client(Web3j web3) {
    if (Objects.isNull(web3)) {
      return CompletableFuture.supplyAsync(() -> false);
    }

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return !getWeb3ClientVersion(web3).get().hasError();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
          }
        });
  }

  private CompletableFuture<Web3ClientVersion> getWeb3ClientVersion(Web3j web3) {
    return web3j.web3ClientVersion().sendAsync();
  }
}
