import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.SELECT_CHAT: {
      return {...reduxStore, selectedChat: action.chatId};
    }

    case actionTypes.SET_USERNAME: {
      return {...reduxStore, username: action.username};
    }

    case actionTypes.SOCKET_OPEN: {
      const socketState = reduxStore.isSocketOpen;
      if (socketState !== undefined) {
        return {...reduxStore, isSocketOpen: !socketState};
      }
      return {...reduxStore, isSocketOpen: false};
    }

    case actionTypes.SOCKET_CLOSED: {
      return {...reduxStore, isSocketOpen: false};
    }

    default:
      return reduxStore;
  }
}
