package ch.anjo.chatchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.0.1.
 */
public class CHATTeRNotaryContract extends Contract {

  private static final String BINARY =
      "608060405234801561001057600080fd5b50610212806100206000396000f3fe60806040526004361061004b5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166303d2107f81146100505780630a4d6c20146100a3575b600080fd5b34801561005c57600080fd5b5061007a6004803603602081101561007357600080fd5b50356100cf565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156100af57600080fd5b506100cd600480360360208110156100c657600080fd5b50356100fc565b005b600090815260208190526040902054610100900473ffffffffffffffffffffffffffffffffffffffff1690565b60008181526020819052604090205460ff161561017a57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601760248201527f4d65737361676520616c72656164792073746f72656421000000000000000000604482015290519081900360640190fd5b6040805180820182526001815233602080830191825260009485528490529190922091518254915173ffffffffffffffffffffffffffffffffffffffff166101000274ffffffffffffffffffffffffffffffffffffffff001991151560ff19909316929092171617905556fea165627a7a723058204edb3032f5af1d49e42e84c7b9b8cfcc62e24f24c553700e3d80d920d48e79f50029";

  public static final String FUNC_GETSENDER = "getSender";

  public static final String FUNC_STOREMESSAGE = "storeMessage";

  @Deprecated
  protected CHATTeRNotaryContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
      BigInteger gasLimit) {
    super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
  }

  protected CHATTeRNotaryContract(String contractAddress, Web3j web3j, Credentials credentials,
      ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  @Deprecated
  protected CHATTeRNotaryContract(String contractAddress, Web3j web3j, TransactionManager transactionManager,
      BigInteger gasPrice, BigInteger gasLimit) {
    super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
  }

  protected CHATTeRNotaryContract(String contractAddress, Web3j web3j, TransactionManager transactionManager,
      ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public RemoteCall<String> getSender(byte[] messageHash) {
    final Function function = new Function(FUNC_GETSENDER,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(messageHash)),
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
        }));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  public RemoteCall<TransactionReceipt> storeMessage(byte[] messageHash) {
    final Function function = new Function(
        FUNC_STOREMESSAGE,
        Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(messageHash)),
        Collections.<TypeReference<?>>emptyList());
    return executeRemoteCallTransaction(function);
  }

  @Deprecated
  public static CHATTeRNotaryContract load(String contractAddress, Web3j web3j, Credentials credentials,
      BigInteger gasPrice, BigInteger gasLimit) {
    return new CHATTeRNotaryContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
  }

  @Deprecated
  public static CHATTeRNotaryContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
      BigInteger gasPrice, BigInteger gasLimit) {
    return new CHATTeRNotaryContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
  }

  public static CHATTeRNotaryContract load(String contractAddress, Web3j web3j, Credentials credentials,
      ContractGasProvider contractGasProvider) {
    return new CHATTeRNotaryContract(contractAddress, web3j, credentials, contractGasProvider);
  }

  public static CHATTeRNotaryContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
      ContractGasProvider contractGasProvider) {
    return new CHATTeRNotaryContract(contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public static RemoteCall<CHATTeRNotaryContract> deploy(Web3j web3j, Credentials credentials,
      ContractGasProvider contractGasProvider) {
    return deployRemoteCall(CHATTeRNotaryContract.class, web3j, credentials, contractGasProvider, BINARY, "");
  }

  @Deprecated
  public static RemoteCall<CHATTeRNotaryContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
      BigInteger gasLimit) {
    return deployRemoteCall(CHATTeRNotaryContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
  }

  public static RemoteCall<CHATTeRNotaryContract> deploy(Web3j web3j, TransactionManager transactionManager,
      ContractGasProvider contractGasProvider) {
    return deployRemoteCall(CHATTeRNotaryContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
  }

  @Deprecated
  public static RemoteCall<CHATTeRNotaryContract> deploy(Web3j web3j, TransactionManager transactionManager,
      BigInteger gasPrice, BigInteger gasLimit) {
    return deployRemoteCall(CHATTeRNotaryContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
  }
}
