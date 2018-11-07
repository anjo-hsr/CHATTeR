import {connect} from 'react-redux';
import MessageInputComponent from '../../components/message/MessageInput';
import {actionMessages} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  addMessage: (chatId, message) => {
    message = {...message, isMe: true};
    dispatch(actionMessages.addMessage({chatId, message}));
  }
});

export const MessageInput = connect(
  reduxStore => {
    return {
      chatId: reduxStore.state.selectedChat,
      username: reduxStore.state.username || ''
    };
  },
  mapDispatchToProps
)(MessageInputComponent);
