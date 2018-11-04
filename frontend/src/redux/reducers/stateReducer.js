import {actionTypes} from '../actions/actions';

export default function reducer(state = {}, action) {
  switch (action.type) {
    case actionTypes.SELECT_CHAT: {
      return {...state, selectedChat: action.chatId};
    }

    default:
      return state;
  }
}
