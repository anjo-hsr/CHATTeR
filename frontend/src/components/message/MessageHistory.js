import React from 'react';
import PropTypes from 'prop-types';

import {Button, Feed, Image, Message, Popup} from 'semantic-ui-react';
import {animateScroll as scroll} from 'react-scroll';

import MessageMoment from './MessageMoment';
import MessageState from '../view/MessageState';
import isOtherAuthorThanBefore from '../../helpers/chatHelper';

import Anonymous from '../../media/anonymous.png';
import moment from 'moment';

export default class MessageHistory extends React.Component {
  isMe = author => {
    return author === this.props.username;
  };

  componentDidUpdate(prevProps) {
    const currentHistory = this.props.chatHistory[this.props.selectedChat] || [];
    const prevHistory = this.props.chatHistory[this.props.selectedChat] || [];
    if (currentHistory.length !== prevHistory.length || this.props.selectedChat !== prevProps.selectedChat) {
      scroll.scrollToBottom({duration: 1000, delay: 2000, smooth: 'easeInOutQuint', containerId: 'messageHistory'});
      console.log('Should scroll now');
    }
  }

  getCurrentChatHistory = () => {
    return this.props.chatHistory[this.props.selectedChat] || [];
  };

  render() {
    let hideInformation = !this.getCurrentChatHistory().some(
      message => typeof message.isMessageOnContract === 'boolean' && !message.isMessageOnContract
    );
    return (
      <Feed>
        <Message
          className="chainInformation"
          color="yellow"
          hidden={hideInformation}
          header="Message not found"
          content="Please wait a few seconds and try again to check the message."
        />
        {this.getCurrentChatHistory().map((message, index, array) => (
          <Feed.Event
            className={this.isMe(message.author) ? 'myMessage' : ''}
            id={isOtherAuthorThanBefore(array, index) ? 'newSender' : 'sameSender'}
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
            <Feed.Label>{isOtherAuthorThanBefore(array, index) && <Image src={Anonymous} />}</Feed.Label>
            <Feed.Content>
              <Feed.Summary>
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
                {isOtherAuthorThanBefore(array, index) &&
                  ' - ' + (this.isMe(message.author) ? 'I' : message.author) + ' wrote'}
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
  checkSignature: PropTypes.func.isRequired,
  username: PropTypes.string.isRequired,
  chatHistory: PropTypes.object,
  chats: PropTypes.object,
  selectedChat: PropTypes.string
};
