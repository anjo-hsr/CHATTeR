import React from 'react';
import {Scrollbars} from 'react-custom-scrollbars';
import {Grid, Header, Image} from 'semantic-ui-react';

import {MessageHistory} from '../../containers/MessageHistory';
import {MessageInput} from '../../containers/MessageInput';
import {avatarBuilder} from '../../helpers/avatarHelpers';
import Anonymous from '../../media/anonymous.png';
import {numbers} from '../../defaults/defaults';
import AnonymousGroup from '../../media/anonymousGroup.png';
import PropTypes from 'prop-types';

export default class MessageWindow extends React.Component {
  render() {
    const chatPartner = this.props.chatPartner;

    return (
      <Grid>
        <div>{'Hallo' + this.props.selectedChat}</div>
        <Grid.Row columns="equal" className="chatInformation">
          <Grid.Column width="2">
            <div className={'chatAvatar ' + (chatPartner.isOnline ? 'online' : 'offline')}>
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
  selectedChat: PropTypes.number.isRequired
};
