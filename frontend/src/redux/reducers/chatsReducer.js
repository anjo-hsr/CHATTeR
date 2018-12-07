import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.ADD_CHAT:
    case actionTypes.CHANGE_CHAT: {
      return Object.assign({}, reduxStore, {[action.chatId]: action.chatInformation});
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

const removeChat = (originalState, id) => {
  let store = {...originalState};
  if (store[id] !== undefined) {
    delete store[id];
  }

  return store;
};
