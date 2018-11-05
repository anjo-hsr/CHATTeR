import React from 'react';
import PropTypes from 'prop-types';

import {Scrollbars} from 'react-custom-scrollbars';
import {Grid, Header, Image} from 'semantic-ui-react';

import {ModalChangeChat} from '../../containers/modal/ModalChangeChat';
import {MessageHistory} from '../../containers/message/MessageHistory';
import {MessageInput} from '../../containers/message/MessageInput';
import {numbers} from '../../defaults/defaults';

import Anonymous from '../../media/anonymous.png';
import AnonymousGroup from '../../media/anonymousGroup.png';

export default class MessageWindow extends React.Component {
  render() {
    return (
      <Grid>
        <Grid.Row columns="equal" className="chatInformation">
          <Grid.Column width="2">
            <Image src={this.props.isGroup ? AnonymousGroup : Anonymous} />
          </Grid.Column>
          <Grid.Column verticalAlign="middle">
            <Header>{'Chat with ' + this.props.chatName}</Header>
          </Grid.Column>
          <Grid.Column width="2">
            <ModalChangeChat />
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="chatHistory" columns="equal">
          <Grid.Column>
            <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
              <MessageHistory />
            </Scrollbars>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="chatInput" columns="equal">
          <Grid.Column>
            <MessageInput />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

MessageWindow.propTypes = {
  selectedChat: PropTypes.string.isRequired,
  chatName: PropTypes.string.isRequired,
  chatPeers: PropTypes.array.isRequired,
  messages: PropTypes.arrayOf(
    PropTypes.shape({
      date: PropTypes.string.isRequired,
      author: PropTypes.string.isRequired,
      message: PropTypes.string.isRequired
    })
  )
};
