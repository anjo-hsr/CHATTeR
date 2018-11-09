import {actionTypes as types} from './actions';
import shajs from 'sha.js';

export default {
  addChat({chatId, chatObject}) {
    return {
      type: types.ADD_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.toString(),
        peers: chatObject.peers
      }
    };
  },

  changeChat({chatId, chatObject}) {
    return {
      type: types.CHANGE_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.toString(),
        peers: chatObject.peers
      }
    };
  },

  deleteChat(chatId) {
    return {
      type: types.DELETE_CHAT,
      chatId
    };
  },

  addChats(chats) {
    return {
      type: types.ADD_CHATS,
      chats
    };
  }
};
