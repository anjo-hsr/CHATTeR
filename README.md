# CHATTeR

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
    
    [mode] [username] [etherAddress]                              [optional: bPort]
    master root       0xc0a71f7eb1a04a867a65022021f962b3a65a40a5  5000
    ```
3. Start each other _Person of Interest_. The default local Port for TomP2P is :5000. If multiple instances
per client would be needed use the optional local Port syntax's to start on different ports. Use therefore these parameters:   
    ```
    mvn exec:java -Dexec.args="______"
    
    [mode] [username] [etherAddress]                              [optional: bPort] [user@rHost:rPort]    [optional: fPort]   
    client harold     0x2329edfb5a326f1f6daf6cec37e50e7cc9f9b151  5001              root@127.0.0.1:5000   
    client john       0x3079c583432ff5eb6a6a338d94f868c81db53f7c  5002              root@127.0.0.1:5000   
    client lionel     0x72143bae18065e62eec38c6e9051e3357c48cd6d  5003              root@127.0.0.1:5000   
    ```
4. Access the frontend by default via [http://localhost:8000](http://localhost:8000/) on the specific host. If no 
  specific port for the frontend was defined it will be always +3000 Port to the backend listening port.


## And now start CHATTeRing by creating new chats.
-- paste here some cool gifs --


Current bug under Windows: mvn initiated Java Thread cannot be closed without being terminated over task manager.
