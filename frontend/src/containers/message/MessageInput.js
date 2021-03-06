import {connect} from 'react-redux';
import {actionMessages} from '../../redux/actions/actions';

import MessageInputComponent from '../../components/message/MessageInput';

const mapDispatchToProps = dispatch => ({
  addMessage: (chatId, message) => {
    dispatch(actionMessages.addMessage({chatId, message}));
  }
});

export const MessageInput = connect(
  reduxStore => {
    return {
      chatId: reduxStore.state.selectedChat,
      username: reduxStore.state.username,
      chatPeers: reduxStore.chats[reduxStore.state.selectedChat].peers.filter(
        peer => peer !== reduxStore.state.username
      )
    };
  },
  mapDispatchToProps
)(MessageInputComponent);
