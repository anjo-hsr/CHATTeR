import React from 'react';
import PropTypes from 'prop-types';

import {slide as Menu} from 'react-burger-menu';
import {isMobile} from 'react-device-detect';
import {Grid} from 'semantic-ui-react';

import {Chats} from '../containers/chat/Chats';
import {MessageWindow} from '../containers/message/MessageWindow';
import {ModalSetConnection} from '../containers/modal/ModalSetConnection';
import webSocketHelper from '../helpers/webSocketHelper';
import {rootSaga} from '../redux/saga/rootSaga';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      newSocketNeeded: true,
      sidebarOpen: true
    };
    this.onSetSidebarOpen = this.onSetSidebarOpen.bind(this);
  }

  onSetSidebarOpen(open) {
    this.setState({sidebarOpen: open});
  }

  setSocket = () => {
    if (Boolean(this.props.username) && this.state.newSocketNeeded) {
      const socket = webSocketHelper(this.props.dispatch, this.props.username);
      this.props.sagaMiddleware.run(rootSaga, socket);

      setTimeout(() => {
        this.props.setConnection(
          this.props.username,
          window.localStorage.getItem('ipAddress'),
          window.localStorage.getItem('portNumber')
        );
        this.setState({newSocketNeeded: false});
      }, 300);
    }
  };

  render() {
    this.setSocket();
    return (
      <div>
        {Boolean(this.props.username) ? (
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
        ) : (
          <ModalSetConnection />
        )}
      </div>
    );
  }
}

App.propTypes = {
  dispatch: PropTypes.func.isRequired,
  sagaMiddleware: PropTypes.func.isRequired,
  setConnection: PropTypes.func.isRequired,
  username: PropTypes.string,
  selectedChat: PropTypes.string
};
