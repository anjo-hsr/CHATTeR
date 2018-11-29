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

  leaveChat({chatId, chatObject, username}) {
    return {
      type: types.LEAVE_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers.filter(peer => peer !== username)
      }
    };
  },

  addChats(chats) {
    return {
      type: types.ADD_CHATS,
      chats
    };
  }
};
