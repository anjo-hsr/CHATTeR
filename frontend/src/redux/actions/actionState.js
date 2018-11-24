import {actionTypes as types} from './actions';

export default {
  toggleSidebar() {
    return {
      type: types.TOGGLE_SIDEBAR,
      hide: undefined
    };
  },

  hideSidebar() {
    return {
      type: types.TOGGLE_SIDEBAR,
      hide: true
    };
  },

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

  setSocketStateOpen(state) {
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
