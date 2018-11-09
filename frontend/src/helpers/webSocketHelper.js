import {webSocketOptions} from '../defaults/defaults';
import {actionState, actionTypes} from '../redux/actions/actions';
import {actionChats, actionMessages} from '../redux/actions/actions';
import actionPeers from '../redux/actions/actionPeers';

export default function webSocketHelper(dispatch) {
  const createSocket = () => {
    return new WebSocket(webSocketOptions.baseString);
  };

  const onMessage = event => {
    dispatch(actionState.setSocketStateOpen(JSON.stringify({type: actionTypes.SOCKET_OPEN})));
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
        console.log(data);
        dispatch(actionMessages.addMessages(data));
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
        dispatch(actionPeers.addPeers(JSON.parse(data.peers)));
        break;
      }
      case actionTypes.SET_USERNAME: {
        dispatch(actionState.setUsername(data.username));
        break;
      }
      default: {
        break;
      }
    }
  };

  let socket = createSocket();
  socket.onmessage = onMessage;

  return socket;
}
