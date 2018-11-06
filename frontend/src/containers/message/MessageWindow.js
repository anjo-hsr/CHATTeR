import {connect} from 'react-redux';

import MessageWindowComponent from '../../components/message/MessageWindow';

export const MessageWindow = connect(
  reduxStore => {
    const chat = reduxStore.chats[reduxStore.state.selectedChat];
    if (Boolean(chat)) {
      return {
        selectedChat: reduxStore.state.selectedChat,
        isGroup: chat.peers.length > 2,
        chatName: chat.name,
        chatPeers: chat.peers
      };
    } else {
      return {
        selectedChat: reduxStore.state.selectedChat,
        isGroup: false,
        chatName: '',
        chatPeers: '',
        messages: []
      };
    }
  },
  {}
)(MessageWindowComponent);
