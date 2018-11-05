import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.ADD_MESSAGE: {
      return concatMessages(reduxStore, action.messageObject);
    }

    case actionTypes.ADD_MESSAGES: {
      return action.messageObjects.reduce((stateCopy, messageObject) => {
        return concatMessages(stateCopy, messageObject);
      }, reduxStore);
    }

    default:
      return reduxStore;
  }
}

const concatMessages = (originalStore, messageObject) => {
  let store = {...originalStore};
  let chat = store[messageObject.chatId];
  if (Boolean(chat)) {
    store[messageObject.chatId] = sortArray(chat.messages, messageObject.message);
  } else {
    store[messageObject.chatId] = [messageObject.message];
  }
  return store;
};

const sortArray = (array, newValue) => {
  array.concat(newValue);
  array.sort((a, b) => {
    return a.date - b.date;
  });
};
