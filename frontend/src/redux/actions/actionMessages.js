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
          .update(message.sender + message.date + message.message + Math.random())
          .digest('hex')
      }
    };
  },

  addMessages(messageObject) {
    return {
      type: types.ADD_MESSAGES,
      messages: messageObject.messages,
      chatId: messageObject.chatId
    };
  }
};
