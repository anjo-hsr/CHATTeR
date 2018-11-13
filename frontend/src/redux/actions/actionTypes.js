let actionTypes = {
  ADD_CHAT: 'ADD_CHAT',
  ADD_CHATS: 'ADD_CHATS',
  APPROVE_CHAT: 'APPROVE_CHAT',
  CHANGE_CHAT: 'CHANGE_CHAT',
  PRE_DELETE_CHAT: 'PRE_DELETE_CHAT',
  DELETE_CHAT: 'DELETE_CHAT',
  SELECT_CHAT: 'SELECT_CHAT',

  ADD_MESSAGE: 'ADD_MESSAGE',
  ADD_MESSAGES: 'ADD_MESSAGES',

  SET_USERNAME: 'SET_USERNAME',
  SET_CONNECTION: 'SET_CONNECTION',

  ADD_PEERS: 'ADD_PEERS',

  TOGGLE_MENU: 'TOGGLE_MENU',

  SOCKET_CLOSED: 'SOCKET_CLOSED',
  SOCKET_OPEN: 'SOCKET_OPEN'
};

export default Object.freeze(Object.assign({}, actionTypes));
