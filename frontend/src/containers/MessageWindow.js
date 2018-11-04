import {connect} from 'react-redux';

import MessageWindowComponent from '../components/message/MessageWindow';

export const MessageWindow = connect(
  state => ({
    selectedChat: state.state.selectedChat
  }),
  {}
)(MessageWindowComponent);
