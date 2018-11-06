import {actionTypes as types} from './actions';

export default {
  addChat({id, chatObject}) {
    return {
      type: types.ADD_CHAT,
      chatObject: {
        id,
        chat: {
          name: chatObject.name || chatObject.peers.toString(),
          peers: chatObject.peers
        }
      }
    };
  },

  changeChat({id, chatObject}) {
    return {
      type: types.CHANGE_CHAT,
      chatObject: {
        id,
        chat: {
          name: chatObject.name || chatObject.peers.toString(),
          peers: chatObject.peers
        }
      }
    };
  },

  deleteChat(id) {
    return {
      type: types.DELETE_CHAT,
      id
    };
  },

  addChats(chats) {
    return {
      type: types.ADD_CHATS,
      chats
    };
  }
};
