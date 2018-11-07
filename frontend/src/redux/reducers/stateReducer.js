import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {}, action) {
  switch (action.type) {
    case actionTypes.TOGGLE_MENU: {
      return {...reduxStore, isMenuOpen: !reduxStore.isMenuOpen || false};
    }

    case actionTypes.SELECT_CHAT: {
      return {...reduxStore, selectedChat: action.chatId};
    }

    case actionTypes.SET_USERNAME: {
      return {...reduxStore, username: action.username};
    }

    case actionTypes.SET_CONNECTION: {
      return {...reduxStore, connection: action.connection};
    }

    default:
      return reduxStore;
  }
}
