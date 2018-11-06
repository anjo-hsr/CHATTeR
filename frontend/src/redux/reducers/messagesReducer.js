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
  let chatArray = store[messageObject.chatId];
  if (Boolean(chatArray)) {
    store[messageObject.chatId] = addMessage(chatArray, messageObject.message);
  } else {
    store[messageObject.chatId] = [messageObject.message];
  }
  return store;
};

const addMessage = (array, newElement) => {
  array.push(newElement);
  return array.sort((element1, element2) => {
    return new Date(element1.date) > new Date(element2.date) ? 1 : -1;
  });
};
