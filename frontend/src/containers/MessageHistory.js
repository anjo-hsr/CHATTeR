import {connect} from 'react-redux';

import MessageHistoryComponent from '../components/message/MessageHistory';

export const MessageHistory = connect(
  state => ({
    chatHistory: state.messages.get(state.selectedChat)
  }),
  {}
)(MessageHistoryComponent);
