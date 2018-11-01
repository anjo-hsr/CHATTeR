import React from 'react';
import {Card, Grid} from 'semantic-ui-react';
import {Scrollbars} from 'react-custom-scrollbars';

import PeerCard from './PeerCard';
import ModalAddAddress from '../modal/ModalAddAddress';
import {numbers} from '../../defaults/defaults';

export default class Peers extends React.Component {
  componentDidMount() {
    this.props.updatePeers();
  }

  render() {
    return (
      <Grid>
        <Grid.Row className="peerOverview">
          <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
            <Card.Group>
              {this.props.peers.map(peer => (
                <PeerCard
                  key={peer._id}
                  peer={peer}
                  selecting={this.props.selecting}
                  updatePeers={this.props.updatePeers}
                />
              ))}
            </Card.Group>
          </Scrollbars>
        </Grid.Row>
        <Grid.Row className="addPeer">
          <Grid.Column verticalAlign="bottom" textAlign="right">
            <ModalAddAddress />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}
