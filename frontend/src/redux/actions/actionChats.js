import {actionTypes as types} from './actions';

export default {
  addChat({chatId, chatObject, approved}) {
    return {
      type: types.ADD_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers,
        approved: approved || false
      }
    };
  },

  changeChat({chatId, chatObject, approved}) {
    return {
      type: types.CHANGE_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers,
        approved: approved || false
      }
    };
  },

  approveChat(chatId) {
    return {
      type: types.APPROVE_CHAT,
      chatId
    };
  },

  preDeleteChat({chatId, chat, username}) {
    let name = chat.name;
    let peers = chat.peers.filter(peer => peer.name !== username);
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
