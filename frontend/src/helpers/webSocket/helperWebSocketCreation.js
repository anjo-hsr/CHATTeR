import {webSocketOptions} from '../../defaults/defaults';
import {actionTypes} from '../../redux/actions/actions';
import {actionChats, actionMessages} from '../../redux/actions/actions';
import actionPeers from '../../redux/actions/actionPeers';

export default function subscribeToServer(dispatch, username) {
  const socket = new WebSocket(`${webSocketOptions.baseString}?username=${username}`);

  socket.onmessage = event => {
    const data = JSON.parse(event.data);
    switch (data.type) {
      case actionTypes.ADD_MESSAGE: {
        dispatch(
          actionMessages.addMessage({
            chatId: data.id,
            message: {date: data.date, author: data.author, message: data.message}
          })
        );
        break;
      }
      case actionTypes.ADD_MESSAGES: {
        dispatch(actionMessages.addMessages(data.messages));
        break;
      }
      case actionTypes.ADD_CHAT: {
        dispatch(actionChats.addChat(data.id, data.name, data.peers));
        break;
      }
      case actionTypes.ADD_CHATS: {
        dispatch(actionChats.addChats(data.chats));
        break;
      }
      case actionTypes.ADD_PEERS: {
        dispatch(actionPeers.addPeers(data.peers));
      }
      default: {
        break;
      }
    }
  };

  socket.onclose = () => {
    socket.send(
      JSON.stringify({
        type: actionTypes.LOGOUT
      })
    );
  };

  return socket;
}
