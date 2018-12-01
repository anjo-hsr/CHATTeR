pragma solidity >=0.4.0 <0.6.0;


contract CHATTeR {
   mapping(bytes32 => address) messages;
    
    function storeMessage(bytes32 messageHash) public {
        messages[messageHash] = msg.sender;
    }
    
    function getSender(bytes32 messageHash) public view returns (address sender) {
        return messages[messageHash];
    }
    
}
