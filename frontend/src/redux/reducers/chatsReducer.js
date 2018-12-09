import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.ADD_CHAT:
    case actionTypes.CHANGE_CHAT: {
      return Object.assign({}, reduxStore, {[action.chatId]: action.chatInformation});
    }

    case actionTypes.ADD_PEERS: {
      return checkForChatState(reduxStore, action.peers);
    }

    case actionTypes.LEAVE_CHAT: {
      return removeChat(reduxStore, action.chatId);
    }

    case actionTypes.ADD_CHATS: {
      return Object.assign({}, reduxStore, action.chats);
    }

    default:
      return reduxStore;
  }
}

const checkForChatState = (originalState, peers) => {
  let store = {...originalState};
  peers.forEach(peer => {
    Object.keys(store).forEach(key => {
      let numberOfOnlinePeers = store[key].numberOfOnlinePeers || 0;
      store[key].peers.forEach(chatPeer => {
        const peerMatch = peer.name === chatPeer;
        if (peerMatch) {
          numberOfOnlinePeers += peer.isOnline ? 1 : 0;
        }
      });
      const isOnline = numberOfOnlinePeers === store[key].peers.length - 1;
      store[key] = {...store[key], isOnline, numberOfOnlinePeers};
    });
  });
  return store;
};

const removeChat = (originalState, id) => {
  let store = {...originalState};
  if (store[id] !== undefined) {
    delete store[id];
  }

  return store;
};
