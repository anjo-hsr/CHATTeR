import {actionTypes} from '../actions/actions';

export default function reducer(state = {}, action) {
  switch (action.type) {
    case actionTypes.ADD_MESSAGE: {
      return concatMessages(state, action.messageObject);
    }

    case actionTypes.ADD_MESSAGES: {
      return action.messageObjects.reduce((stateCopy, messageObject) => {
        return concatMessages(stateCopy, messageObject);
      }, state);
    }

    default:
      return state;
  }
}

const concatMessages = (originalState, messageObject) => {
  let state = {...originalState};
  let chat = state[messageObject.chatId];
  if (Boolean(chat)) {
    state[messageObject.chatId] = sortArray(chat.messages, messageObject.message);
  } else {
    state[messageObject.chatId] = [messageObject.message];
  }
  return state;
};

const sortArray = (array, newValue) => {
  array.concat(newValue);
  array.sort((a, b) => {
    return a.date - b.date;
  });
};
