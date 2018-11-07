import React from 'react';
import PropTypes from 'prop-types';

import {isMobile} from 'react-device-detect';
import {Grid, Sidebar} from 'semantic-ui-react';

import {ModalSetConnection} from '../containers/modal/ModalSetConnection';
import webSocketHelper from '../helpers/webSocketHelper';
import viewHelper from '../helpers/viewHelper';
import {rootSaga} from '../redux/saga/rootSaga';
import {Chats} from '../containers/chat/Chats';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      newSocketNeeded: true,
      sidebarOpen: true,
      iconName: 'bars',
      visible: false
    };
    this.getHeader = viewHelper.getHeader.bind(this);
    this.getSideBar = viewHelper.getSideBar.bind(this);
    this.hideSidebar = viewHelper.hideSidebar.bind(this);
    this.toggleVisibility = viewHelper.toggleVisibility.bind(this);
    this.getMessageWindow = viewHelper.getMessageWindow.bind(this);
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
          <Grid className="siteGrid" padded>
            {this.getHeader(true)}
            {isMobile ? (
              <Grid.Row className="siteContent" columns="equal">
                <Grid.Column>
                  <Sidebar.Pushable>
                    {this.getSideBar()}
                    <Sidebar.Pusher onClick={this.hideSidebar}>{this.getMessageWindow()}</Sidebar.Pusher>
                  </Sidebar.Pushable>
                </Grid.Column>
              </Grid.Row>
            ) : (
              <Grid.Row className="siteContent" columns="equal">
                <Grid.Column width="4">
                  <Chats />
                </Grid.Column>
                <Grid.Column width="1" />
                {this.getMessageWindow()}
                <Grid.Column width="1" />
              </Grid.Row>
            )}
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
