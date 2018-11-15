import React from 'react';

import {Button, Grid, Header, Image, Sidebar} from 'semantic-ui-react';
import {isMobile} from 'react-device-detect';
import {Chats} from '../containers/chat/Chats';
import {MessageWindow} from '../containers/message/MessageWindow';

import CHATTeR from '../media/CHATTeR.png';

export default {
  getHeader(username) {
    return (
      <Grid.Row columns="equal" verticalAlign="middle" className="siteHeader">
        {(isMobile || this.state.isSmallWindow) && (
          <Grid.Column width="3" textAlign="center" className="headerButton">
            <Button color="green" icon={this.state.iconName} onClick={this.toggleVisibility} />
          </Grid.Column>
        )}
        <Grid.Column textAlign="center" className="headerElement">
          <Grid>
            <Grid.Row columns="equal" verticalAlign="middle">
              <Grid.Column width="1" />
              <Grid.Column textAlign="right">
                <Header className="username" size="medium" color="green" content={`${username}`} />
              </Grid.Column>
              <Grid.Column>
                <Image className="chatterImage" src={CHATTeR} floated="left" />
              </Grid.Column>
              <Grid.Column width="1" />
            </Grid.Row>
          </Grid>
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
      <Sidebar animation="overlay" width={isMobile ? 'thin' : 'wide'} visible={this.state.visible}>
        <Chats />
      </Sidebar>
    );
  },

  getMessageWindow() {
    return <Grid.Column className="messageWindow">{Boolean(this.props.selectedChat) && <MessageWindow />}</Grid.Column>;
  }
};
