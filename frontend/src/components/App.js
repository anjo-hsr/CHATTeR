import React from 'react';
import PropTypes from 'prop-types';

import {isMobile} from 'react-device-detect';
import {Grid, Sidebar} from 'semantic-ui-react';

import {Chats} from '../containers/chat/Chats';
import webSocketHelper from '../helpers/webSocketHelper';
import viewHelper from '../helpers/viewHelper';
import {rootSaga} from '../redux/saga/rootSaga';
import ModalWaitForWebSocket from './modal/ModalWaitForWebSocket';
import {numbers} from '../defaults/defaults';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      newSocketNeeded: true,
      sidebarOpen: true,
      iconName: 'bars',
      visible: false,
      socket: null,
      isSmallWindow: false,
      refreshIntervalId: null
    };
    this.getHeader = viewHelper.getHeader.bind(this);
    this.getSideBar = viewHelper.getSideBar.bind(this);
    this.hideSidebar = viewHelper.hideSidebar.bind(this);
    this.toggleVisibility = viewHelper.toggleVisibility.bind(this);
    this.getMessageWindow = viewHelper.getMessageWindow.bind(this);
  }

  componentDidMount() {
    this.setSocket();
    let refreshIntervalId = setInterval(() => {
      console.log('Retry connection to webSocket');
      this.setSocket();
    }, numbers.reloadTimer);
    this.setState({refreshIntervalId, isSmallWindow: window.innerWidth < 900});
  }

  setSocket = () => {
    if (!Boolean(this.props.username)) {
      const socket = webSocketHelper(this.props.dispatch);
      this.props.sagaMiddleware.run(rootSaga, socket);
      this.props.setSocketStateOpen();
      setTimeout(() => {
        this.setState({socket, newSocketNeeded: false});
      }, 50);
    }
  };

  render() {
    return (
      <div>
        {Boolean(this.props.username) ? (
          <Grid className="siteGrid" padded>
            {clearInterval(this.state.refreshIntervalId)}
            {this.getHeader(this.props.username)}
            {isMobile || this.state.isSmallWindow ? (
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
          <div>
            <ModalWaitForWebSocket />
          </div>
        )}
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
