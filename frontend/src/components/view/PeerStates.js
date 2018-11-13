import React from 'react';
import {Button, Card, Popup} from 'semantic-ui-react';

import PeerCard from './PeerCard';

export default class PeerStates extends React.Component {
  render() {
    return (
      <Popup trigger={<Button icon="info" color="blue" size="large" inverted />} on="click" size="large" wide="very">
        <Card.Group className="peerInformation" itemsPerRow="3" stackable>
          {this.props.chatPeers.map(peer => (
            <PeerCard key={peer.name + Math.random().toString()} peer={peer} />
          ))}
        </Card.Group>
      </Popup>
    );
  }
}
