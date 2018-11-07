import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.ADD_MESSAGE: {
      return concatMessages(reduxStore, action.chatId, action.messageInformation);
    }

    case actionTypes.ADD_MESSAGES: {
      return action.messages.reduce((stateCopy, messageInformation) => {
        return concatMessages(stateCopy, action.chatId, messageInformation);
      }, reduxStore);
    }

    default:
      return reduxStore;
  }
}

const concatMessages = (originalStore, chatId, message) => {
  let store = {...originalStore};
  let chatArray = store[chatId];
  if (Boolean(chatArray)) {
    if (!checkIfAlreadyInStore(store, chatId, message)) {
      store[chatId] = addMessage(chatArray, message);
    }
  } else {
    store[chatId] = [message];
  }
  return store;
};

const checkIfAlreadyInStore = (store, chatId, message) => {
  return store[chatId].some(element => {
    return equalMessages(element, message);
  });
};

const equalMessages = (leftSide, rightSide) => {
  return (
    leftSide.date === rightSide.date &&
    leftSide.author === rightSide.author &&
    leftSide.message === rightSide.message &&
    leftSide.isMe === rightSide.isMe
  );
};

const addMessage = (array, newElement) => {
  array.push(newElement);
  return array.sort((element1, element2) => {
    return new Date(element1.date) > new Date(element2.date) ? 1 : -1;
  });
};
