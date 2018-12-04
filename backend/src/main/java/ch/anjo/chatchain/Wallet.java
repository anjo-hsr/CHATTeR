package ch.anjo.chatchain;

import java.io.Serializable;
import java.util.Objects;

class Wallet implements Serializable {

  private final String walletPath;
  private final String password;

  Wallet(String walletString, String password) {
    if (Objects.nonNull(password) && !password.equals("")) {
      this.walletPath = "./backend/src/main/resources/wallets/" + walletString;
      this.password = password;
      return;
    }
    this.walletPath = null;
    this.password = null;
  }

  String getWalletPath() {
    return walletPath;
  }

  String getPassword() {
    return password;
  }
}
