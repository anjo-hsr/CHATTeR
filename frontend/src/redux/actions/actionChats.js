import {actionTypes as types} from './actions';

export default {
  addChat({id, name, peers, lastMessage}) {
    return {
      type: types.ADD_CHAT,
      id,
      name: name || peers.toString(),
      peers,
      lastMessage: lastMessage || ''
    };
  },

  addChats(chats) {
    return {
      type: types.ADD_CHATS,
      chats
    };
  }
};
