import {actionState} from '../../redux/actions/actions';
import connect from 'react-redux/es/connect/connect';
import ChatCardComponent from '../../components/chat/ChatCard';

const selectChat = dispatch => ({
  selectChat: chatId => {
    dispatch(actionState.selectChat(chatId));
  }
});

export const ChatCard = connect(
  reduxStore => {
    return {
      self: reduxStore.state.username
    };
  },
  selectChat
)(ChatCardComponent);
