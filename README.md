# <img width="200" src="images/CHATTeR.png">

## Requirements

- Java 11
- A modern browser - so no IE 8 :(

## Build and run the application:

1. Build the project:
    ```
    mvn clean install
    ```
2. Start the main DHT with the following arguments:
    ```
    mvn exec:java -Dexec.args="______"
    
    [mode] [username] [optional: backendPort]
    master ernest     5000
    ```
3. Start each other _Person of Interest_. The default local Port for TomP2P is :5000. If multiple instances
per client would be needed use the optional local Port syntax's to start on different ports. Use therefore these parameters:   
    ```
    mvn exec:java -Dexec.args="______"
    
    [mode] [username] [optional: backendPort]  [user@rHost:remotePort]     [optional: frontendPort]   
    client root       5001                     ernest@127.0.0.1:5000  
    client harold     5002                     ernest@127.0.0.1:5000  
    client john       5003                     ernest@127.0.0.1:5000  
    client lionel     5004                     ernest@127.0.0.1:5000  
    client sameen     5005                     ernest@127.0.0.1:5000  
    ```
4. Access the frontend by default via http://localhost:8000/ on the specific host. If no 
  specific port for the frontend was defined it will be always +3000 Port to the backend listening port.


## And now start CHATTeRing by creating new chats.
-- paste here some cool gifs --


## Sources:
- [Ethereum Blockchain “Hello World” Smart Contract with JAVA](https://medium.com/coinmonks/ethereum-blockchain-hello-world-smart-contract-with-java-9b6ae2961ad1)
- [Nodeless Ethereum Smart Contracts Development with Infura](https://medium.com/stacktical/nodeless-ethereum-smart-contracts-development-with-infura-d22d6a1fec10)
- [Using Infura with web3j](https://docs.web3j.io/infura.html#)

## Bugs:
- Windows: mvn initiated Java Thread cannot be closed without being terminated over task manager.
