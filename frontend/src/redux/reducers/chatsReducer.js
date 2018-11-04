import {actionTypes} from '../actions/actions';

export default function reducer(state = [], action) {
  switch (action.type) {
    case actionTypes.ADD_CHAT: {
      return state.concat([
        {
          id: action.id,
          name: action.name,
          peers: action.peers,
          lastMessage: action.lastMessage
        }
      ]);
    }

    case actionTypes.ADD_CHATS: {
      return state.concat(
        action.chats.map(chat => {
          return {
            id: chat.id,
            chats: chat.chats
          };
        })
      );
    }

    default:
      return state;
  }
}
