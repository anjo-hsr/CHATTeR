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
        {isMobile || this.state.isSmallWindow ? (
          <Grid.Column width="2" textAlign="center" className="headerButton">
            <Button color="green" icon={this.props.sidebarIcon} onClick={this.toggleVisibility} />
          </Grid.Column>
        ) : (
          <Grid.Column width="4" />
        )}
        <Grid.Column width="6" className="chatterImageContainer" textAlign="center">
          <Image className="chatterImage" src={CHATTeR} verticalAlign="middle" />
        </Grid.Column>

        <Grid.Column textAlign="right">
          <Header className="username" size="medium" color="green" content={`${username}`} />
        </Grid.Column>
        <Grid.Column width="1" />
      </Grid.Row>
    );
  },

  toggleVisibility() {
    this.props.toggleSidebar();
  },

  hideSidebar() {
    this.props.hideSidebar();
  },

  getSideBar() {
    return (
      <Sidebar animation="overlay" width={isMobile ? 'thin' : 'wide'} visible={this.props.isSidebarOpen}>
        <Chats />
      </Sidebar>
    );
  },

  getMessageWindow() {
    return <Grid.Column className="messageWindow">{Boolean(this.props.selectedChat) && <MessageWindow />}</Grid.Column>;
  }
};
