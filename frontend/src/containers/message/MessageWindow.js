import {connect} from 'react-redux';

import MessageWindowComponent from '../../components/message/MessageWindow';
import {actionChats} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  approveChat: chatId => {
    dispatch(actionChats.approveChat(chatId));
  }
});

export const MessageWindow = connect(
  reduxStore => {
    const chat = reduxStore.chats[reduxStore.state.selectedChat];
    return {
      selectedChat: reduxStore.state.selectedChat,
      isGroup: chat.peers.length > 2,
      chatName: chat.name,
      chatPeers: chat.peers.filter(peer => peer !== reduxStore.state.username),
      chatApproved: chat.approved
    };
  },
  mapDispatchToProps
)(MessageWindowComponent);
