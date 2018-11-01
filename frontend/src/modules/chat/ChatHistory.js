import React from 'react';
import {Button, Feed, Image, Popup} from 'semantic-ui-react';
import moment from 'moment';

import Loader from '../view/Loader';
import {avatarBuilder, isAvatarNeeded} from '../../helpers/avatarHelpers';
import {apiPatch} from '../../helpers/apiHelpers';

import Anonymous from '../../media/anonymous.png';

export default class ChatHistory extends React.Component {
  deleteMessage = messageId => {
    apiPatch.deleteMessage(messageId);
    setTimeout(() => {
      this.props.updateChat(this.props.chatPartner.address);
      this.props.updatePeers();
    }, 500);
  };

  render() {
    const chatPartner = this.props.chatPartner;
    const chatHistory = [...this.props.chatHistory];

    return (
      <Feed>
        {!this.props.isChatHistoryLoaded ? (
          <Loader />
        ) : (
          chatHistory.map((element, index, array) => (
            <Feed.Event
              className={element.isMe ? 'myMessage' : ''}
              key={element.date + element.message + Math.random()}
            >
              <Button
                inverted
                circular
                size="mini"
                color="red"
                icon="trash alternate outline"
                onClick={() => this.deleteMessage(element._id)}
              />
              <Feed.Label>
                {isAvatarNeeded(array, index) && (
                  <div className={'peerAvatar ' + (element.isMe || chatPartner.isOnline ? 'online' : 'offline')}>
                    {element.isMe ? (
                      avatarBuilder.getOwnAvatar()
                    ) : Boolean(chatPartner.avatar) ? (
                      avatarBuilder.getOtherAvatar(chatPartner.avatar)
                    ) : (
                      <Image src={Anonymous} />
                    )}
                  </div>
                )}
              </Feed.Label>
              <Feed.Content>
                <Feed.Summary>
                  {(element.isMe ? 'I' : chatPartner.name) + ' wrote'}
                  <Feed.Date>
                    <i>
                      <Popup
                        position="top center"
                        trigger={<div>{moment(element.date, 'YYYY-MM-DD HH:mm:ss').fromNow()}</div>}
                        content={element.date}
                      />
                    </i>
                  </Feed.Date>
                </Feed.Summary>
                <Feed.Extra text>{element.message}</Feed.Extra>
              </Feed.Content>
            </Feed.Event>
          ))
        )}
      </Feed>
    );
  }
}
