package ch.anjo.chatter.tomp2p;

import blockchain.Constants;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class NotaryService {
  private final Web3j web3j;
  private final Credentials credentials;
  //private final Notary notary;

  public NotaryService(String etherAddress, String walletPassword){
    web3j = Web3j.build(new HttpService())
  }


}
