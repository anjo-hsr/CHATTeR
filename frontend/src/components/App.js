import React from 'react';
import PropTypes from 'prop-types';

import {Grid} from 'semantic-ui-react';

import {Chats} from '../containers/chat/Chats';
import {MessageWindow} from '../containers/message/MessageWindow';
import {ModalSetUsername} from '../containers/modal/ModalSetUsername';
import {creator, helper} from '../helpers/webSocketHelper';

export default class App extends React.Component {
  render() {
    if (Boolean(this.props.username)) {
      const socket = creator(this.props.dispatch, this.props.username);
      this.props.middleware.run(helper, {socket, username: this.props.username});
    }
    return (
      <div>
        {Boolean(this.props.username) ? (
          <Grid className="siteGrid">
            <Grid.Row columns="equal">
              <Grid.Column className="chatChats" width="4">
                <Chats />
              </Grid.Column>
              <Grid.Column width="1" />
              <Grid.Column className="chatWindow">{Boolean(this.props.selectedChat) && <MessageWindow />}</Grid.Column>
              <Grid.Column width="1" />
            </Grid.Row>
          </Grid>
        ) : (
          <ModalSetUsername />
        )}
      </div>
    );
  }
}

App.PropTypes = {
  dispatch: PropTypes.func.isRequired,
  username: PropTypes.string,
  selectedChat: PropTypes.string.isRequired
};
