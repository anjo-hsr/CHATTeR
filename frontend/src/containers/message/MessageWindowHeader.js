import {connect} from 'react-redux';
import {actionChats} from '../../redux/actions/actions';

import MessageWindowHeaderComponent from '../../components/message/MessageWindowHeader';

const getChatPeers = (chatPeers, peers, username) => {
  let mappedPeers = chatPeers.filter(chatPeer => chatPeer !== username).map(chatPeer => {
    return {name: chatPeer};
  });
  mappedPeers = mappedPeers.map(chatPeer => {
    let matchedPeer = peers.find(peer => peer.name === chatPeer);
    const isOnline = Boolean(matchedPeer) ? matchedPeer.isOnline : false;
    return {...chatPeer, isOnline};
  });

  return mappedPeers;
};

const mapDispatchToProps = dispatch => ({
  approveChat: chatId => {
    dispatch(actionChats.approveChat(chatId));
  }
});

export const MessageWindowHeader = connect(
  reduxStore => {
    const chat = reduxStore.chats[reduxStore.state.selectedChat];
    return {
      selectedChat: reduxStore.state.selectedChat,
      isGroup: chat.peers.length > 2,
      chatName: chat.name,
      chatPeers: getChatPeers(chat.peers, reduxStore.peers, reduxStore.state.username),
      chatApproved: chat.approved,
      isSidebarOpen: reduxStore.state.isSidebarOpen
    };
  },
  mapDispatchToProps
)(MessageWindowHeaderComponent);
