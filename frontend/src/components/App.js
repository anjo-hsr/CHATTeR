import React from 'react';
import {Grid} from 'semantic-ui-react';

import {Chats} from '../containers/Chats';
import {MessageWindow} from '../containers/MessageWindow';
import ModalSetUsername from './modal/ModalSetUsername';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      chatPartnerSelected: false,
      chatPartner: {
        address: null,
        name: null,
        avatar: null,
        isGroup: null,
        isOnline: null
      },
      isChatHistoryLoaded: false,
      username: undefined
    };
  }
  setUsername = username => {
    //sendMessage(this.state.webSocket, {messageType: actionTypes.CHANGE_NAME, message: username});
    this.setState({username});
  };

  render() {
    return (
      <div>
        {Boolean(this.state.username) ? (
          <Grid className="siteGrid">
            <Grid.Row columns="equal">
              <Grid.Column className="chatChats" width="4">
                <Chats />
              </Grid.Column>
              <Grid.Column width="1" />
              <Grid.Column className="chatWindow">
                {!Boolean(state.state.selectedChat) && <MessageWindow />}
              </Grid.Column>
              <Grid.Column width="1" />
            </Grid.Row>
          </Grid>
        ) : (
          <ModalSetUsername username={this.state.username} setUsername={this.setUsername} />
        )}
      </div>
    );
  }
}
