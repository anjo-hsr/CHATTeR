import {connect} from 'react-redux';

import ChatsComponent from '../../components/chat/Chats';

export const Chats = connect(
  reduxStore => {
    return {
      keys: Object.keys(reduxStore.chats),
      chats: reduxStore.chats,
      selectedChat: reduxStore.state.selectedChat
    };
  },
  {}
)(ChatsComponent);
