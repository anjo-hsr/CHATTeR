import React from 'react';
import {Scrollbars} from 'react-custom-scrollbars';
import {Grid, Header, Image} from 'semantic-ui-react';

import ChatHistory from './ChatHistory';
import ChatInput from './ChatInput';
import {avatarBuilder} from '../../helpers/avatarHelpers';
import Anonymous from '../../media/anonymous.png';
import {numbers} from '../../defaults/defaults';
import AnonymousGroup from '../../media/anonymousGroup.png';

export default class ChatWindow extends React.Component {
  render() {
    const chatPartner = this.props.chatPartner;

    return (
      <Grid>
        <Grid.Row columns="equal" className="chatInformation">
          <Grid.Column width="2">
            <div className={'peerAvatar ' + (chatPartner.isOnline ? 'online' : 'offline')}>
              {Boolean(chatPartner.avatar) ? (
                <div>{avatarBuilder.getOtherAvatar(chatPartner.avatar)}</div>
              ) : (
                <Image src={chatPartner.isGroup ? AnonymousGroup : Anonymous} />
              )}
            </div>
          </Grid.Column>
          <Grid.Column verticalAlign="middle">
            <Header>
              {'Chat with ' + chatPartner.name}
              {!chatPartner.isOnline && <i> - currently offline</i>}
            </Header>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="chatHistory" columns="equal">
          <Grid.Column>
            <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
              <ChatHistory
                chatPartner={chatPartner}
                chatHistory={this.props.chatHistory}
                isChatHistoryLoaded={this.props.isChatHistoryLoaded}
                updateChat={this.props.updateChat}
                updatePeers={this.props.updatePeers}
              />
            </Scrollbars>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="chatInput" columns="equal">
          <Grid.Column>
            <ChatInput
              chatPartner={chatPartner}
              isChatHistoryLoaded={this.props.isChatHistoryLoaded}
              updateChat={this.props.updateChat}
              updatePeers={this.props.updatePeers}
            />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}
