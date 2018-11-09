import React from 'react';
import PropTypes from 'prop-types';

import {slide as Menu} from 'react-burger-menu';
import {isMobile} from 'react-device-detect';
import {Grid} from 'semantic-ui-react';

import {Chats} from '../containers/chat/Chats';
import {MessageWindow} from '../containers/message/MessageWindow';
import webSocketHelper from '../helpers/webSocketHelper';
import {rootSaga} from '../redux/saga/rootSaga';
import ModalWaitForWebSocket from './modal/ModalWaitForWebSocket';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      newSocketNeeded: true,
      sidebarOpen: true,
      isSocketOpen: true,
      socket: null
    };
    this.onSetSidebarOpen = this.onSetSidebarOpen.bind(this);
  }

  onSetSidebarOpen(open) {
    this.setState({sidebarOpen: open});
  }

  setSocket = () => {
    if (this.props.isSocketOpen === undefined && this.state.newSocketNeeded) {
      const socket = webSocketHelper(this.props.dispatch);
      this.props.sagaMiddleware.run(rootSaga, socket);
      this.props.setSocketStateOpen();
      setTimeout(() => {
        this.setState({socket, newSocketNeeded: false});
      }, 50);
    }
  };

  render() {
    this.setSocket();
    return (
      <div>
        <Grid className="siteGrid">
          <Grid.Row columns="equal">
            {isMobile ? (
              <Menu>
                <Chats />
              </Menu>
            ) : (
              <Grid.Column width="5">
                <Chats />
              </Grid.Column>
            )}
            <Grid.Column width={isMobile ? 5 : 1} />
            <Grid.Column className="chatWindow">{Boolean(this.props.selectedChat) && <MessageWindow />}</Grid.Column>
            <Grid.Column width="1" />
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}

App.propTypes = {
  dispatch: PropTypes.func.isRequired,
  isSocketOpen: PropTypes.bool,
  setSocketStateOpen: PropTypes.func.isRequired,
  sagaMiddleware: PropTypes.func.isRequired,
  username: PropTypes.string,
  selectedChat: PropTypes.string
};
