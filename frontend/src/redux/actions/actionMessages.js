import {actionTypes as types} from './actions';

export default {
  addMessage({chatId, message}) {
    return {
      type: types.ADD_MESSAGE,
      messageObject: {
        chatId,
        message
      }
    };
  },

  addMessages(messages) {
    return {
      type: types.ADD_MESSAGES,
      messages
    };
  }
};
