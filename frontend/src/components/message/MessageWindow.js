import React from 'react';
import PropTypes from 'prop-types';

import {Scrollbars} from 'react-custom-scrollbars';
import {Button, Grid, Header, Image} from 'semantic-ui-react';

import PeerStates from '../../components/view/PeerStates';
import {ModalChangeChat} from '../../containers/modal/ModalChangeChat';
import {MessageHistory} from '../../containers/message/MessageHistory';
import {MessageInput} from '../../containers/message/MessageInput';
import {numbers} from '../../defaults/defaults';

import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';

export default class MessageWindow extends React.Component {
  render() {
    return (
      <Grid className={this.props.isSidebarOpen ? 'background' : ''}>
        <Grid.Row columns="equal" className="messageInformation">
          <Grid.Column width="2" verticalAlign="middle">
            <Image src={this.props.isGroup ? AnonymousGroup : Anonymous} />
          </Grid.Column>
          <Grid.Column verticalAlign="middle">
            <Header>{'Chat with ' + this.props.chatName}</Header>
          </Grid.Column>
          <Grid.Column width="2" verticalAlign="middle">
            <ModalChangeChat />
          </Grid.Column>
          <Grid.Column width="2" verticalAlign="middle">
            <PeerStates chatPeers={this.props.chatPeers} />
          </Grid.Column>
          {this.props.chatApproved && (
            <Grid.Column width="2" verticalAlign="middle">
              <Button
                icon="sign in alternate"
                color="green"
                size="large"
                inverted
                onClick={() => this.props.approveChat(this.props.selectedChat)}
              />
            </Grid.Column>
          )}
        </Grid.Row>
        <Grid.Row className="messageHistory" columns="equal">
          <Grid.Column>
            <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
              <MessageHistory />
            </Scrollbars>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="messageInput" columns="equal">
          <Grid.Column>
            <MessageInput />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

MessageWindow.propTypes = {
  approveChat: PropTypes.func,
  chatApproved: PropTypes.bool,
  selectedChat: PropTypes.string.isRequired,
  chatName: PropTypes.string.isRequired,
  chatPeers: PropTypes.array.isRequired,
  messages: PropTypes.arrayOf(
    PropTypes.shape({
      date: PropTypes.string.isRequired,
      author: PropTypes.string.isRequired,
      message: PropTypes.string.isRequired
    })
  ),
  isSidebarOpen: PropTypes.bool.isRequired
};
