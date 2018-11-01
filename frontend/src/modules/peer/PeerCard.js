import React from 'react';
import {Button, Card, Image} from 'semantic-ui-react';

import {avatarBuilder} from '../../helpers/avatarHelpers';

import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';
import {apiPatch} from '../../helpers/apiHelpers';
import {numbers} from '../../defaults/defaults';

export default class PeerCard extends React.Component {
  deletePeer = peerId => {
    apiPatch.deletePeer(peerId);
    setTimeout(() => {
      this.props.updatePeers();
    }, 500);
  };

  getLastMessage = lastMessage => {
    let content = lastMessage.content;
    if (content.length > numbers.maxLastMessageLength) {
      let limit = numbers.maxLastMessageLength - 3;
      limit = content[limit] === ' ' ? limit + 1 : limit;
      content = content.substring(0, limit) + '...';
    }
    if (!Boolean(lastMessage.isMe)) {
      return;
    }
    return content + ' - from ' + (lastMessage.isMe ? 'myself' : 'peer');
  };

  render() {
    return (
      <Card fluid>
        <Card.Content>
          <Card.Header content={this.props.peer.name} />
          <Card.Description content={this.getLastMessage(this.props.peer.lastMessage)} />
          <div className={'peerAvatar ' + (this.props.peer.isOnline ? 'online' : 'offline')}>
            {Boolean(this.props.peer.avatar) ? (
              <div>{avatarBuilder.getOtherAvatar(this.props.peer.avatar)}</div>
            ) : (
              <Image src={this.props.peer.isGroup ? AnonymousGroup : Anonymous} />
            )}
          </div>
        </Card.Content>
        <Card.Content extra>
          <div className="ui two buttons">
            <Button
              inverted
              color="green"
              content="Chat"
              onClick={() =>
                this.props.selecting(
                  this.props.peer.address,
                  this.props.peer.name,
                  this.props.peer.avatar,
                  this.props.peer.isGroup,
                  this.props.peer.isOnline
                )
              }
            />
            <Button inverted color="red" content="Remove" onClick={() => this.deletePeer(this.props.peer._id)} />
          </div>
        </Card.Content>
      </Card>
    );
  }
}
