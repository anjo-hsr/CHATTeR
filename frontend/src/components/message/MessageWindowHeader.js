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
    );
  }
}

MessageWindowHeader.propTypes = {
  approveChat: PropTypes.func,
  chatApproved: PropTypes.bool,
  selectedChat: PropTypes.string.isRequired,
  chatName: PropTypes.string.isRequired,
  chatPeers: PropTypes.array.isRequired
};
