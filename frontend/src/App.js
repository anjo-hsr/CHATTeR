import React from 'react';
import {Grid} from 'semantic-ui-react';

import Peers from './modules/peer/Peers';
import ChatWindow from './modules/chat/ChatWindow';
import {apiGet} from './helpers/apiHelpers';

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
      peers: []
    };
  }

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

  updatePeers = () => {
    apiGet.getPeers().then(res => {
      this.setState({peers: JSON.parse(res.request.response)});
    });
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
    );
  }
}

export default App;
