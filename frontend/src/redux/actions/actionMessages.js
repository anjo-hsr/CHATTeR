import shajs from 'sha.js';
import {actionTypes as types} from './actions';

export default {
  addMessage({chatId, message}) {
    return {
      type: types.ADD_MESSAGE,
      chatId,
      messageInformation: {
        ...message,
        messageId: shajs('sha256')
          .update(chatId + message.author + new Date(message.date) + message.message)
          .digest('hex'),
        signedBy: []
      }
    };
  },

  addMessageInbound({chatId, message}) {
    return {
      type: types.ADD_MESSAGE_INBOUND,
      chatId,
      messageInformation: {
        ...message,
        messageId: shajs('sha256')
          .update(chatId + message.author + new Date(message.date) + message.message)
          .digest('hex'),
        signedBy: []
      }
    };
  },

  addMessages(messageObject) {
    return {
      type: types.ADD_MESSAGES,
      messages: messageObject.messages,
      chatId: messageObject.chatId
    };
  },

  confirmMessage({chatId, messageId, username}) {
    return {
      type: types.CONFIRM_MESSAGE,
      chatId,
      messageId,
      username
    };
  },

  checkSignature({chatId, messageId, senderAddress}) {
    return {
      type: types.CHECK_SIGNATURE,
      chatId,
      messageId,
      senderAddress: senderAddress //senderAddress.substring(0, 39) + 'a'
    };
  },

  responseCheckSignature({chatId, messageId, isSenderCorrect, isMessageOnContract}) {
    return {
      type: types.RESPONSE_CHECK_SIGNATURE,
      chatId,
      messageId,
      isSenderCorrect,
      isMessageOnContract
    };
  }
};
