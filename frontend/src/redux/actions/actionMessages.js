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
