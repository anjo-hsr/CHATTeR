import React from 'react';
import PropTypes from 'prop-types';

import {Button, Feed, Image, Popup} from 'semantic-ui-react';
import moment from 'moment';

import Loader from '../view/Loader';
import {isAvatarNeeded} from '../../helpers/avatarHelpers';

import Anonymous from '../../media/anonymous.png';

export default class MessageHistory extends React.Component {
  render() {
    const chatPartner = this.props.chatPartner;
    const messages = [...this.props.messages];

    return (
      <Feed>
        {!this.props.isChatHistoryLoaded ? (
          <Loader />
        ) : (
          messages.map((message, index, array) => (
            <Feed.Event
              className={message.isMe ? 'myMessage' : ''}
              key={message.date + message.message + Math.random()}
            >
              <Button
                inverted
                circular
                size="mini"
                color="red"
                icon="trash alternate outline"
                onClick={() => this.deleteMessage(message.chatId)}
              />
              <Feed.Label>{isAvatarNeeded(array, index) && <Image src={Anonymous} />}</Feed.Label>
              <Feed.Content>
                <Feed.Summary>
                  {(message.isMe ? 'I' : chatPartner.name) + ' wrote'}
                  <Feed.Date>
                    <i>
                      <Popup
                        position="top center"
                        trigger={<div>{moment(message.date, 'YYYY-MM-DD HH:mm:ss').fromNow()}</div>}
                        content={message.date}
                      />
                    </i>
                  </Feed.Date>
                </Feed.Summary>
                <Feed.Extra text>{message.message}</Feed.Extra>
              </Feed.Content>
            </Feed.Event>
          ))
        )}
      </Feed>
    );
  }
}
