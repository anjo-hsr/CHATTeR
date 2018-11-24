import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = {sidebarIcon: 'bars', isSidebarOpen: false, username: ''}, action) {
  switch (action.type) {
    case actionTypes.TOGGLE_SIDEBAR: {
      let iconNames = new Map([[false, 'bars'], [true, 'close']]);

      let isSidebarOpen = !reduxStore.isSidebarOpen;
      if (Boolean(action.hide)) {
        isSidebarOpen = false;
      }

      return {...reduxStore, isSidebarOpen, sidebarIcon: iconNames.get(isSidebarOpen)};
    }

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
