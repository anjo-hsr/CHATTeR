import {connect} from 'react-redux';

import ChatsComponent from '../components/chat/Chats';

export const Chats = connect(
  state => ({
    chats: state.chats
  }),
  {}
)(ChatsComponent);
