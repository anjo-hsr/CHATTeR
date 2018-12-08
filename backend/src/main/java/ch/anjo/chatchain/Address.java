package ch.anjo.chatchain;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

  private final String address;
  private final Wallet wallet;

  public static String nullAddress = "0x0000000000000000000000000000000000000000";

  Address(String address, String prefix, String password) {
    if (isAddressCorrect(address)) {
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

  Wallet getWallet() {
    return wallet;
  }

  String getShortAddress() {
    return String.format("%s...", address.substring(0, 20));
  }

  private boolean isAddressCorrect(String address) {
    return address.startsWith("0x") && address.length() == 42;
  }
}
