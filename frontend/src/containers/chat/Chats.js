import {connect} from 'react-redux';

import ChatsComponent from '../../components/chat/Chats';

export const Chats = connect(
  reduxStore => {
    return {
      chats: reduxStore.chats,
      selectedChat: reduxStore.state.selectedChat
    };
  },
  {}
)(ChatsComponent);
