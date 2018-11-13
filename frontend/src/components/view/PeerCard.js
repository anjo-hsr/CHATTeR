import React from 'react';
import {Button, Card, Image} from 'semantic-ui-react';

import Anonymous from '../../media/anonymous.png';

export default class PeerStates extends React.Component {
  render() {
    return (
      <Card className="peerCard">
        <Card.Content>
          <Image
            className={'chatAvatar' + this.props.peer.isOnline ? '' : '.offline'}
            floated="right"
            size="mini"
            src={Anonymous}
          />
          <Card.Header content={this.props.peer.name} />
        </Card.Content>
      </Card>
    );
  }
}
