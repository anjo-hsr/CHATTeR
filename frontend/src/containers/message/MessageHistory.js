import {connect} from 'react-redux';

import MessageHistoryComponent from '../../components/message/MessageHistory';
import {actionMessages} from '../../redux/actions/actions';

const getSenderAddress = (peers, author) => {
  return peers.find(peer => peer.name === author);
};

const mapDispatchToProps = dispatch => ({
  checkSignature: ({chatId, messageId, author, peers}) => {
    const sender = getSenderAddress(peers, author);
    dispatch(actionMessages.checkSignature({chatId, messageId, senderAddress: sender.etherAddress}));
  }
});

export const MessageHistory = connect(
  reduxStore => {
    return {
      username: reduxStore.state.username,
      chatHistory: reduxStore.messages,
      chats: reduxStore.chats,
      selectedChat: reduxStore.state.selectedChat,
      peers: reduxStore.peers
    };
  },
  mapDispatchToProps
)(MessageHistoryComponent);
