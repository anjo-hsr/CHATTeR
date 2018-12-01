pragma solidity ^0.5.0;

library Library {
    struct data {
        bool isValue;
        address sender;
    }
}

contract CHATTeR {

    using Library for Library.data;

    mapping(bytes32 => Library.data) messages;

    function storeMessage(bytes32 messageHash) public {
        require(!messages[messageHash].isValue, "Message already stored!");
        messages[messageHash] = Library.data(true, msg.sender);
    }

    function getSender(bytes32 messageHash) public view returns (address sender) {
        return messages[messageHash].sender;
    }

}
