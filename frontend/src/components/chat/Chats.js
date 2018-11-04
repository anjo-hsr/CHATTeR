import React from 'react';
import {Card, Grid} from 'semantic-ui-react';
import {Scrollbars} from 'react-custom-scrollbars';
import PropTypes from 'prop-types';

import {ChatCard} from '../../containers/ChatCard';
import ModalAddAddress from '../modal/ModalAddAddress';
import {numbers} from '../../defaults/defaults';

export default class Chats extends React.Component {
  render() {
    return (
      <Grid>
        <Grid.Row className="chatOverview">
          <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
            <Card.Group>
              {this.props.chats.map(chat => (
                <ChatCard key={chat.id} chat={chat} />
              ))}
            </Card.Group>
          </Scrollbars>
        </Grid.Row>
        <Grid.Row className="addChat">
          <Grid.Column verticalAlign="bottom" textAlign="right">
            <ModalAddAddress />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

Chats.propTypes = {
  chats: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      name: PropTypes.string,
      peers: PropTypes.arrayOf(
        PropTypes.shape({
          name: PropTypes.string.isRequired
        })
      ).isRequired,
      lastMessage: PropTypes.string.isRequired
    }).isRequired
  ).isRequired
};
