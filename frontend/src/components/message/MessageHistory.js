import React from 'react';
import PropTypes from 'prop-types';

import {Button, Feed, Icon, Image, Popup} from 'semantic-ui-react';

import MessageMoment from './MessageMoment';
import isAvatarNeeded from '../../helpers/avatarHelpers';

import Anonymous from '../../media/anonymous.png';
import moment from 'moment';

export default class MessageHistory extends React.Component {
  isMe = author => {
    return author === this.props.username;
  };

  getCurrentChatHistory = () => {
    return this.props.chatHistory[this.props.selectedChat] || [];
  };

  render() {
    return (
      <Feed>
        {this.getCurrentChatHistory().map((message, index, array) => (
          <Feed.Event
            className={this.isMe(message.author) ? 'myMessage' : ''}
            key={message.date + message.message + Math.random()}
          >
            {this.isMe(message.author) ? (
              <Icon inverted circular color="grey" name="check" />
            ) : (
              <Button circular color="grey" icon="pencil alternate" />
            )}

            <Feed.Label>{isAvatarNeeded(array, index) && <Image src={Anonymous} />}</Feed.Label>
            <Feed.Content>
              <Feed.Summary>
                {(this.isMe(message.author) ? 'I' : message.author) + ' wrote'}
                <Feed.Date>
                  <i>
                    <Popup
                      position="top center"
                      trigger={<MessageMoment date={message.date} />}
                      content={
                        <div>
                          {moment(message.date)
                            .format('YYYY-MM-DD HH:mm:ss')
                            .toString()}
                        </div>
                      }
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

MessageHistory.propTypes = {
  username: PropTypes.string.isRequired,
  chatHistory: PropTypes.object,
  selectedChat: PropTypes.string
};
