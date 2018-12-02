//Contract deployed with Remix at address 0x7ca169776f530A05aC930f0069ACce8277FCC2cd

pragma solidity >0.4.99 <0.6.0;

contract CHATTeRNotaryContract {
    struct data {
        bool isValue;
        address sender;
    }

    mapping(bytes32 => data) messages;

    function storeMessage(bytes32 messageHash) public {
        require(!messages[messageHash].isValue, "Message already stored!");
        messages[messageHash] = data(true, msg.sender);
    }

    function getSender(bytes32 messageHash) public view returns (address sender) {
        return messages[messageHash].sender;
    }
}
