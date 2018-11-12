import {actionTypes as types} from './actions';

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

  preDeleteChat({chatId, chat, username}) {
    let name = chat.name;
    let peers = chat.peers.filter(peer => peer !== username);
    if (peers.length <= 1) {
      return {
        type: types.DELETE_CHAT,
        chatId
      };
    }
    return {
      type: types.CHANGE_CHAT,
      chatId,
      chatInformation: {name, peers}
    };
  },

  addChats(chats) {
    return {
      type: types.ADD_CHATS,
      chats
    };
  }
};
