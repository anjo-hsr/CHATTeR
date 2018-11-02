import React from 'react';
import {Grid} from 'semantic-ui-react';

import Peers from './modules/peer/Peers';
import ChatWindow from './modules/chat/ChatWindow';
import {apiGet} from './helpers/apiHelpers';
import {subscribeToServer, sendMessage} from './helpers/webSocketHelper';
import ModalSetUsername from './modules/modal/ModalSetUsername';
import {messageTypes} from './defaults/defaults';

class App extends React.Component {
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
      chatHistory: [],
      isChatHistoryLoaded: false,
      peers: [],
      username: undefined,
      webSocket: null
    };
  }

  componentDidMount() {
    this.setState({webSocket: subscribeToServer()});
  }

  setUsername = username => {
    sendMessage(this.state.webSocket, {messageType: messageTypes.setName, message: username});
    this.setState({username});
  };

  updatePeers = () => {
    sendMessage(this.state.webSocket, {messageType: messageTypes.getPeers, message: 'Please send peers.'});
  };

  selectChatPartner = (address, name, avatar, isGroup, isOnline) => {
    this.setState({
      chatPartnerSelected: true,
      chatPartner: {
        address,
        name,
        avatar,
        isGroup,
        isOnline
      },
      chatHistory: [],
      isChatHistoryLoaded: false
    });
    this.updateChat(address);
  };

  updateChat = address => {
    apiGet.getChatHistory(address).then(res => {
      this.setState({
        chatHistory: JSON.parse(res.request.response),
        isChatHistoryLoaded: true
      });
    });
  };

  render() {
    return (
      <div>
        {Boolean(this.state.username) ? (
          <Grid className="siteGrid">
            <Grid.Row columns="equal">
              <Grid.Column className="chatPeers" width="4">
                <Peers peers={this.state.peers} updatePeers={this.updatePeers} selecting={this.selectChatPartner} />
              </Grid.Column>
              <Grid.Column width="1" />
              <Grid.Column className="chatWindow">
                {this.state.chatPartnerSelected && (
                  <ChatWindow
                    chatPartner={this.state.chatPartner}
                    chatHistory={this.state.chatHistory}
                    isChatHistoryLoaded={this.state.isChatHistoryLoaded}
                    updateChat={this.updateChat.bind(this)}
                    updatePeers={this.updatePeers}
                  />
                )}
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

export default App;
