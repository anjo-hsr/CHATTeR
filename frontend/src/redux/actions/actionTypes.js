let actionTypes = {
  ADD_CHAT: 'ADD_CHAT',
  ADD_CHATS: 'ADD_CHATS',
  CHANGE_CHAT: 'CHANGE_CHAT',
  DELETE_CHAT: 'DELETE_CHAT',
  SELECT_CHAT: 'SELECT_CHAT',

  ADD_MESSAGE: 'ADD_MESSAGE',
  ADD_MESSAGES: 'ADD_MESSAGES',

  SET_USERNAME: 'SET_USERNAME',
  SET_CONNECTION: 'SET_CONNECTION',

  ADD_PEERS: 'ADD_PEERS'
};

export default Object.freeze(Object.assign({}, actionTypes));
