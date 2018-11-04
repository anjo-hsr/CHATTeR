import React from 'react';
import {Button, Card, Image} from 'semantic-ui-react';

import {avatarBuilder} from '../../helpers/avatarHelpers';

import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';
import PropTypes from 'prop-types';

export default class ChatCard extends React.Component {
  render() {
    return (
      <Card fluid>
        <Card.Content>
          <Card.Header content={this.props.chat.name || this.props.chat.peers[0]} />
          <Card.Description content={this.props.chat.lastMessage} />
          <div className={'chatAvatar ' + (this.props.chat.isOnline ? 'online' : 'offline')}>
            {Boolean(this.props.chat.avatar) ? (
              <div>{avatarBuilder.getOtherAvatar(this.props.chat.avatar)}</div>
            ) : (
              <Image src={this.props.chat.peers.length > 1 ? AnonymousGroup : Anonymous} />
            )}
          </div>
        </Card.Content>
        <Card.Content extra>
          <div className="ui two buttons">
            <Button inverted color="green" content="Chat" onClick={() => this.props.selectChat(this.props.chat.id)} />
            <Button inverted color="red" content="Remove" onClick={() => this.deleteChat(this.props.chat.id)} />
          </div>
        </Card.Content>
      </Card>
    );
  }
}

ChatCard.propTypes = {
  selectChat: PropTypes.func.isRequired
};
