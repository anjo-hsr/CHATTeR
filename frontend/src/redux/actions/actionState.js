import {actionTypes as types} from './actions';

export default {
  selectChat(chatId) {
    return {
      type: types.SELECT_CHAT,
      chatId
    };
  }
};
