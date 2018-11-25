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
          .update(chatId + message.sender + message.date + message.message)
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
  }
};
