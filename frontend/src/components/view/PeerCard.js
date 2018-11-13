import React from 'react';
import {Card, Image} from 'semantic-ui-react';

import Anonymous from '../../media/anonymous.png';

export default class PeerStates extends React.Component {
  render() {
    return (
      <Card className="peerCard">
        <Card.Content>
          <Card.Header>
            <Image
              className={'chatAvatar' + this.props.peer.isOnline ? '' : '.offline'}
              floated="right"
              size="mini"
              src={Anonymous}
            />
            {this.props.peer.name}
          </Card.Header>
        </Card.Content>
      </Card>
    );
  }
}
