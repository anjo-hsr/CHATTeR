import {actionTypes as types} from './actions';

export default {
  toggleMenu() {
    return {
      type: types.TOGGLE_MENU
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

  setConnection(name, masterIpAddress, masterPortNumber) {
    return {
      type: types.SET_CONNECTION,
      connection: {
        name,
        masterIpAddress,
        masterPortNumber
      }
    };
  }
};
