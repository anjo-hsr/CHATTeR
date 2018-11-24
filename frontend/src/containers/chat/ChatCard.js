import {actionChats, actionState} from '../../redux/actions/actions';
import connect from 'react-redux/es/connect/connect';

import ChatCardComponent from '../../components/chat/ChatCard';

const mapStateToProps = dispatch => ({
  selectChat: chatId => {
    dispatch(actionState.hideSidebar());
    dispatch(actionState.selectChat(chatId));
  },
  deleteChat: (chatId, chat, self) => {
    dispatch(actionState.selectChat(null));
    dispatch(actionChats.preDeleteChat({chatId, chat, username: self}));
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
