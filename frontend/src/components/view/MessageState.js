import React from 'react';
import {Button, Card, Icon, Popup} from 'semantic-ui-react';

import PeerCard from './PeerCard';

export default class MessageState extends React.Component {
  render() {
    return (
      <Popup
        trigger={<Button icon="check" circular color={this.props.didAllSign ? 'blue' : 'grey'} />}
        on="click"
        size="large"
        wide="very"
      >
        <Card.Group className="peerInformation" itemsPerRow="3" stackable>
          {this.props.readers.map(reader => (
            <PeerCard key={reader + Math.random().toString()} peer={{name: reader}} />
          ))}
        </Card.Group>
      </Popup>
    );
  }
}
