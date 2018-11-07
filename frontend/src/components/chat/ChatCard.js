import React from 'react';
import PropTypes from 'prop-types';

import {Button, Card, Image} from 'semantic-ui-react';

import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';

export default class ChatCard extends React.Component {
  getChatMembers = () => {
    const chatPartners = this.props.chat.peers
      .filter(peer => peer !== this.props.self)
      .sort((peerA, peerB) => peerA - peerB);
    return chatPartners.concat('Me').toString();
  };

  render() {
    return (
      <Card fluid>
        <Card.Content>
          <Card.Header content={this.props.chat.name || this.props.chat.peers[0]} />
          <Card.Description content={this.getChatMembers()} />
          <div className={'chatAvatar ' + (this.props.chat.isOnline ? 'online' : 'offline')}>
            <Image src={this.props.chat.peers.length > 2 ? AnonymousGroup : Anonymous} />
          </div>
        </Card.Content>
        <Card.Content extra>
          <div className="ui two buttons">
            <Button inverted color="red" content="Remove" onClick={() => this.props.deleteChat(this.props.chatId)} />
            <Button inverted color="green" content="Chat" onClick={() => this.props.selectChat(this.props.chatId)} />
          </div>
        </Card.Content>
      </Card>
    );
  }
}

ChatCard.propTypes = {
  chatId: PropTypes.string.isRequired,
  deleteChat: PropTypes.func.isRequired,
  selectChat: PropTypes.func.isRequired,
  self: PropTypes.string.isRequired
};
