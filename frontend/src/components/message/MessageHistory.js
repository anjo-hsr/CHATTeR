import React from 'react';
import PropTypes from 'prop-types';

import {Button, Feed, Image, Message, Popup} from 'semantic-ui-react';

import MessageMoment from './MessageMoment';
import MessageState from '../view/MessageState';
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
    let hideInformation = !this.getCurrentChatHistory().some(
      message => typeof message.isMessageOnContract === 'boolean' && !message.isMessageOnContract
    );
    return (
      <div>
        <Message
          className="chainInformation"
          color="yellow"
          hidden={hideInformation}
          header="Message not found"
          content="Please wait a few seconds and try again to check the message."
        />
        <Feed>
          {this.getCurrentChatHistory().map((message, index, array) => (
            <Feed.Event
              className={this.isMe(message.author) ? 'myMessage' : ''}
              key={message.date + message.message + Math.random()}
            >
              {this.isMe(message.author) ? (
                <MessageState
                  readers={message.signedBy}
                  possibleReaders={message.possibleReaders}
                  didAllSign={message.signedBy.length === message.possibleReaders.length}
                />
              ) : (
                <Button
                  circular
                  disabled={Boolean(message.isSenderCorrect) || Boolean(message.isMessageOnContract)}
                  color={
                    Boolean(message.isSenderCorrect)
                      ? 'blue'
                      : Boolean(message.isMessageOnContract)
                      ? 'red'
                      : typeof message.isMessageOnContract === 'boolean'
                      ? 'yellow'
                      : 'grey'
                  }
                  icon={
                    Boolean(message.isSenderCorrect)
                      ? 'handshake outline'
                      : Boolean(message.isMessageOnContract)
                      ? 'broken chain'
                      : 'chain'
                  }
                  onClick={() =>
                    this.props.checkSignature({
                      chatId: this.props.selectedChat,
                      messageId: message.messageId,
                      author: message.author,
                      peers: this.props.peers
                    })
                  }
                />
              )}
              <Feed.Label>{isAvatarNeeded(array, index) && <Image src={Anonymous} />}</Feed.Label>
              <Feed.Content>
                <Feed.Summary>
                  {(this.isMe(message.author) ? 'I' : message.author) + ' wrote'}
                  <Feed.Date>
                    <i>
                      <Popup
                        position="top center"
                        trigger={
                          <div>
                            <MessageMoment date={message.date} />
                          </div>
                        }
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
      </div>
    );
  }
}

MessageHistory.propTypes = {
  checkSignature: PropTypes.func.isRequired,
  username: PropTypes.string.isRequired,
  chatHistory: PropTypes.object,
  chats: PropTypes.object,
  selectedChat: PropTypes.string
};
