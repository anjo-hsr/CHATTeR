import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.CHANGE_CHAT:
    case actionTypes.ADD_CHAT: {
      return concatChats(reduxStore, action.chatObject);
    }

    case actionTypes.ADD_CHATS: {
      return reduxStore.concat(
        action.chats.map(chat => {
          return {
            id: chat.id,
            chats: chat.chats
          };
        })
      );
    }

    default:
      return reduxStore;
  }
}

const concatChats = (originalStore, chatObject) => {
  let store = {...originalStore};
  let chat = store[chatObject.id];
  if (Boolean(chat)) {
    store[chatObject.id] = chatObject.chat;
  } else {
    store[chatObject.id] = chatObject.chat;
  }
  return store;
};
