import {connect} from 'react-redux';

import MessageWindowComponent from '../../components/message/MessageWindow';

export const MessageWindow = connect(
  reduxStore => {
    return {
      isSidebarOpen: reduxStore.state.isSidebarOpen
    };
  },
  {}
)(MessageWindowComponent);
