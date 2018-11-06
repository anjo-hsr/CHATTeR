import {actionChats, actionState} from '../../redux/actions/actions';
import connect from 'react-redux/es/connect/connect';
import ChatCardComponent from '../../components/chat/ChatCard';

const mapStateToProps = dispatch => ({
  selectChat: chatId => {
    dispatch(actionState.selectChat(chatId));
  },
  deleteChat: chatId => {
    dispatch(actionState.selectChat(null));
    dispatch(actionChats.deleteChat(chatId));
  }
});

export const ChatCard = connect(
  reduxStore => {
    return {
      self: reduxStore.state.username
    };
  },
  mapStateToProps
)(ChatCardComponent);
