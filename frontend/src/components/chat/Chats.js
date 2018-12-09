import React from 'react';
import PropTypes from 'prop-types';

import {Card, Grid} from 'semantic-ui-react';
import {Scrollbars} from 'react-custom-scrollbars';

import {ChatCard} from '../../containers/chat/ChatCard';
import {ModalAddChat} from '../../containers/modal/ModalAddChat';
import {numbers} from '../../defaults/defaults';

export default class Chats extends React.Component {
  render() {
    return (
      <Grid className="chatContainer" padded>
        <Grid.Row className="chatOverview">
          <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
            <Card.Group>
              {Object.keys(this.props.chats).map(chatId => (
                <ChatCard
                  key={chatId}
                  chatId={chatId}
                  selected={this.props.selectedChat === chatId}
                  chat={this.props.chats[chatId]}
                />
              ))}
            </Card.Group>
          </Scrollbars>
        </Grid.Row>
        <Grid.Row className="addChat">
          <Grid.Column verticalAlign="bottom" textAlign="right">
            <ModalAddChat />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

Chats.propTypes = {
  chats: PropTypes.objectOf(
    PropTypes.shape({
      name: PropTypes.string,
      peers: PropTypes.arrayOf(PropTypes.string)
    }).isRequired
  ).isRequired,
  selectedChat: PropTypes.string
};
