import {actionChats, actionState} from '../../redux/actions/actions';
import connect from 'react-redux/es/connect/connect';

import ChatCardComponent from '../../components/chat/ChatCard';

const mapStateToProps = dispatch => ({
  selectChat: chatId => {
    dispatch(actionState.hideSidebar());
    dispatch(actionState.selectChat(chatId));
  },
  leaveChat: (chatId, chatObject, username) => {
    dispatch(actionState.selectChat(null));
    dispatch(actionChats.leaveChat({chatId, chatObject, username}));
  }
});

export const ChatCard = connect(
  reduxStore => {
    return {
      username: reduxStore.state.username
    };
  },
  mapStateToProps
)(ChatCardComponent);
