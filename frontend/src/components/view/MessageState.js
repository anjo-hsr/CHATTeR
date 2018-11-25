import React from 'react';
import {Button, Card, Popup} from 'semantic-ui-react';

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
            <PeerCard
              key={reader + Math.random().toString()}
              peer={{name: reader, didRead: true}}
              showReadState={true}
            />
          ))}
          {this.props.possibleReaders.map(reader => {
            if (this.props.readers.indexOf(reader) === -1) {
              return (
                <PeerCard
                  key={reader + Math.random().toString()}
                  peer={{name: reader, didRead: false}}
                  showReadState={true}
                />
              );
            }
            return null;
          })}
        </Card.Group>
      </Popup>
    );
  }
}
