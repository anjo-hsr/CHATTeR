package ch.anjo.chatchain;

import java.util.Map;
import java.util.Objects;

public class Constants {

  public static final String INFURA_ADDRESS =
      "https://ropsten.infura.io/v3/d5bc11e49faf41e6bb4e1bf26ca63604";

  public static final Map<String, Address> walletMap = Map.ofEntries(
      Map.entry("contract", new Address("0x7ca169776f530A05aC930f0069ACce8277FCC2cd",
          null, "ch.anjo.CHATTeR")),
      Map.entry("ernest", new Address("0xffcec74d5d93f714831e73d711daf8c841ccb274",
          "UTC--2018-11-07T21-55-57.710879800Z--", "ch.anjo.CHATTeR")),
      Map.entry("harold", new Address("0x84e43852103686202476adbf9f9a3ef072600c26",
          "UTC--2018-11-07T21-55-46.252551000Z--", "ch.anjo.CHATTeR")),
      Map.entry("john", new Address("0x0c2a47237ce9a2820ea1f8d9666356c2c43be001",
          "UTC--2018-11-07T21-55-51.086337200Z--", "ch.anjo.CHATTeR")),
      Map.entry("lionel", new Address("0x9cd489a1d50b3069b803e38fdff462a107fc5816",
          "UTC--2018-11-07T21-56-01.920737900Z--", "ch.anjo.CHATTeR")),
      Map.entry("root", new Address("0xb02642ec68a0a3a23a5c74d3aa4ce337aa64dc7e",
          "UTC--2018-11-07T21-55-36.402539500Z--", "ch.anjo.CHATTeR")),
      Map.entry("sameen", new Address("0xeb55003a1b4d75f08731efee466b573ff0283f82",
          "UTC--2018-11-07T21-56-10.279871900Z--", "ch.anjo.CHATTeR"))
  );
}

class Wallet {

  private final String walletString;
  private final String prefixWalletPath = "src/main/java/ch/anjo/chatchain/wallets/";
  private final String walletPath;
  private final String password;

  Wallet(String walletString, String password) {
    if (Objects.nonNull(password) && !password.equals("")) {
      this.walletString = walletString;
      this.walletPath = prefixWalletPath + walletString;
      this.password = password;
      return;
    }
    this.walletString = null;
    this.walletPath = null;
    this.password = null;
  }

  String getWalletString() {
    return walletString;
  }

  String getPassword() {
    return password;
  }
}

class Address {

  private final String address;
  private final Wallet wallet;

  Address(String address, String prefix, String password) {
    if (address.startsWith("0x") && address.length() == 40) {
      this.address = address;
      if (Objects.nonNull(prefix)) {
        this.wallet = new Wallet(prefix + address.substring(2), password);
      } else {
        this.wallet = null;
      }
      return;
    }
    this.address = null;
    this.wallet = null;
  }

  public String getAddress() {
    return address;
  }

  public Wallet getWallet() {
    return wallet;
  }
}
