import {actionTypes as types} from './actions';

export default {
  selectChat(chatId) {
    return {
      type: types.SELECT_CHAT,
      chatId
    };
  },

  setUsername(username) {
    return {
      type: types.SET_USERNAME,
      username
    };
  },

  setSocketStateOpen() {
    return {
      type: types.SOCKET_OPEN
    };
  },

  setSocketStateClosed() {
    return {
      type: types.SOCKET_CLOSED
    };
  }
};
