import {actionTypes as types} from './actions';

export default {
  addMessage({chatId, message}) {
    return {
      type: types.ADD_MESSAGE,
      chatId,
      messageInformation: message
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
