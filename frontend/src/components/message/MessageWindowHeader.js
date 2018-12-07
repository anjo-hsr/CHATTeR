import React from 'react';
import PropTypes from 'prop-types';

import {Button, Grid, Header, Image} from 'semantic-ui-react';

import {ModalChangeChat} from '../../containers/modal/ModalChangeChat';
import PeerStates from '../view/PeerStates';
import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';

export default class MessageWindowHeader extends React.Component {
  render() {
    return (
      <Grid.Row columns="equal" className="messageInformation">
        <Grid.Column width="2" verticalAlign="middle">
          <Image src={this.props.isGroup ? AnonymousGroup : Anonymous} />
        </Grid.Column>
        <Grid.Column verticalAlign="middle">
          <Header>{'Chat with ' + this.props.chatName}</Header>
        </Grid.Column>
        <Grid.Column width="2" verticalAlign="middle">
          <Button
            icon="sign out alternate"
            color="red"
            size="large"
            inverted
            onClick={() =>
              this.props.leaveChat({
                chatId: this.props.selectedChat,
                chatObject: this.props.chatObject,
                username: this.props.username
              })
            }
          />
        </Grid.Column>
        <Grid.Column width="2" verticalAlign="middle">
          <ModalChangeChat />
        </Grid.Column>
        <Grid.Column width="2" verticalAlign="middle">
          <PeerStates chatPeers={this.props.chatPeers} />
        </Grid.Column>
      </Grid.Row>
    );
  }
}

MessageWindowHeader.propTypes = {
  leaveChat: PropTypes.func,
  selectedChat: PropTypes.string.isRequired,
  chatName: PropTypes.string.isRequired,
  chatPeers: PropTypes.array.isRequired,
  username: PropTypes.string.isRequired,
  chatObject: PropTypes.object.isRequired
};
