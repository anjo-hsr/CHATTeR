import React from 'react';
import PropTypes from 'prop-types';

import {Feed, Icon, Image, Popup} from 'semantic-ui-react';
import moment from 'moment';

import Loader from '../view/Loader';
import {isAvatarNeeded} from '../../helpers/avatarHelpers';

import Anonymous from '../../media/anonymous.png';

export default class MessageHistory extends React.Component {
  isMe = author => {
    return author === this.props.username;
  };

  render() {
    const messages = [...this.props.chatHistory];

    return (
      <Feed>
        {messages.map((message, index, array) => (
          <Feed.Event
            className={this.isMe(message.author) ? 'myMessage' : ''}
            key={message.date + message.message + Math.random()}
          >
            <Icon
              inverted
              circular
              color={message.received ? 'blue' : 'grey'}
              className={this.isMe(message.author) ? 'myMessage' : ''}
              name={message.received ? 'check circle outline' : 'check circle'}
            />
            <Feed.Label>{isAvatarNeeded(array, index) && <Image src={Anonymous} />}</Feed.Label>
            <Feed.Content>
              <Feed.Summary>
                {(this.isMe(message.author) ? 'I' : message.author) + ' wrote'}
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
        ))}
      </Feed>
    );
  }
}

MessageHistory.PropTypes = {
  username: PropTypes.string.isRequired,
  chatHistory: PropTypes.arrayOf(
    PropTypes.shape({
      date: PropTypes.string.isRequired,
      author: PropTypes.string.isRequired,
      message: PropTypes.string.isRequired
    })
  )
};
