import {actionTypes as types} from './actions';

export default {
  addChat({chatId, chatObject}) {
    return {
      type: types.ADD_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers
      }
    };
  },

  addChatInbound({chatId, chatObject}) {
    return {
      type: types.ADD_CHAT_INBOUND,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers
      }
    };
  },

  changeChat({chatId, chatObject}) {
    return {
      type: types.CHANGE_CHAT,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers
      }
    };
  },

  changeChatInbound({chatId, chatObject}) {
    return {
      type: types.CHANGE_CHAT_INBOUND,
      chatId,
      chatInformation: {
        name: chatObject.name || chatObject.peers.map(peer => peer.name).toString(),
        peers: chatObject.peers
      }
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
