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

    case actionTypes.CONFIRM_MESSAGE: {
      return updateMessage(reduxStore, action.chatId, action.messageId, action.username);
    }

    case actionTypes.RESPONSE_CHECK_SIGNATURE: {
      return addCheckOfSignature(
        reduxStore,
        action.chatId,
        action.messageId,
        action.isSenderCorrect,
        action.isMessageOnContract
      );
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
    return element.messageId === message.messageId;
  });
};

const addMessage = (array, newElement) => {
  array.push(newElement);
  return array.sort((element1, element2) => {
    return new Date(element1.date) > new Date(element2.date) ? 1 : -1;
  });
};

const updateMessage = (originalStore, chatId, messageId, signer) => {
  let store = {...originalStore};
  let chatArray = store[chatId];
  if (Boolean(chatArray)) {
    store[chatId] = chatArray.map(message => {
      if (message.messageId === messageId) {
        message.signedBy = [...message.signedBy, signer];
      }
      return message;
    });
  }
  return store;
};

const addCheckOfSignature = (originalStore, chatId, messageId, isSenderCorrect, isMessageOnContract) => {
  let store = {...originalStore};
  let chatArray = store[chatId];
  if (Boolean(chatArray)) {
    store[chatId] = chatArray.map(message => {
      if (message.messageId === messageId) {
        message.isSenderCorrect = isSenderCorrect;
        message.isMessageOnContract = isMessageOnContract;
      }
      return message;
    });
  }
  return store;
};
