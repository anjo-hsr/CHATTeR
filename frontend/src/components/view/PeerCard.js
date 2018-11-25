import React from 'react';
import {Card, Icon, Image} from 'semantic-ui-react';

import Anonymous from '../../media/anonymous.png';

export default class PeerStates extends React.Component {
  render() {
    return (
      <Card className="peerCard">
        <Card.Content>
          <Card.Header>
            {this.props.peer.name}
            <Image
              className={'chatAvatar' + this.props.peer.isOnline ? '' : '.offline'}
              floated="right"
              size="mini"
              src={Anonymous}
            />
            {Boolean(this.props.showReadState) && (
              <Icon
                name="check"
                circular
                inverted
                color={Boolean(this.props.peer.didRead) ? 'blue' : 'grey'}
                size="small"
              />
            )}
          </Card.Header>
        </Card.Content>
      </Card>
    );
  }
}
