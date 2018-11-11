import React from 'react';

import {Button, Grid, Header, Sidebar} from 'semantic-ui-react';
import {isMobile} from 'react-device-detect';
import {Chats} from '../containers/chat/Chats';
import {MessageWindow} from '../containers/message/MessageWindow';

export default {
  getHeader(username) {
    return (
      <Grid.Row columns="equal" verticalAlign="middle" className="siteHeader">
        {isMobile && (
          <Grid.Column width="3" textAlign="center" className="headerButton">
            <Button color="green" icon={this.state.iconName} onClick={this.toggleVisibility} />
          </Grid.Column>
        )}
        <Grid.Column textAlign="center" className="headerElement">
          <Header size="medium" color="green" content={`CHATTeR - ${username}`} />
        </Grid.Column>
      </Grid.Row>
    );
  },

  toggleVisibility() {
    let iconNames = new Map([['bars', 'close'], ['close', 'bars']]);
    this.setState({visible: !this.state.visible});
    this.setState({iconName: iconNames.get(this.state.iconName)});
  },

  hideSidebar() {
    let iconNames = new Map([['bars', 'close'], ['close', 'bars']]);
    this.setState({visible: false});
    this.setState({iconName: iconNames.get('close')});
  },

  getSideBar() {
    return (
      <Sidebar animation="overlay" width="thin" visible={this.state.visible}>
        <Chats />
      </Sidebar>
    );
  },

  getMessageWindow() {
    return <Grid.Column className="messageWindow">{Boolean(this.props.selectedChat) && <MessageWindow />}</Grid.Column>;
  }
};
