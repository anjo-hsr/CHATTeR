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
      username: reduxStore.state.username || '',
      chatApproved: reduxStore.chats[reduxStore.state.selectedChat].approved
    };
  },
  mapDispatchToProps
)(MessageInputComponent);
