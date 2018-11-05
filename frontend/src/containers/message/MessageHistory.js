import {connect} from 'react-redux';

import MessageHistoryComponent from '../../components/message/MessageHistory';

export const MessageHistory = connect(
  reduxStore => {
    return {
      username: reduxStore.state.username,
      chatHistory: reduxStore.messages[reduxStore.state.selectedChat] || []
    };
  },
  {}
)(MessageHistoryComponent);
