import {webSocketOptions} from '../../defaults/defaults';
import {actionTypes} from '../../redux/actions/actions';
import {actionChats, actionMessages} from '../../redux/actions/actions';

export default function subscribeToServer(dispatch, username) {
  const socket = new WebSocket(webSocketOptions.baseString);

  socket.onopen = () => {
    socket.send(
      JSON.stringify({
        type: actionTypes.CHANGE_NAME,
        username
      })
    );
  };

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
        dispatch(actionChats.addChat(data.id, data.name, data.peers, data.lastMessage));
        break;
      }
      case actionTypes.ADD_CHATS: {
        dispatch(actionChats.addChats(data.chats));
        break;
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
