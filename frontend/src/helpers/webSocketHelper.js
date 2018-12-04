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
    console.log(data);
    switch (data.type) {
      case actionTypes.ADD_MESSAGE: {
        dispatch(
          actionMessages.addMessage({
            chatId: data.chatId,
            message: {
              date: data.messageInformation.date,
              author: data.messageInformation.author,
              message: data.messageInformation.message,
              messageId: data.messageInformation.messageId
            }
          })
        );
        break;
      }
      case actionTypes.ADD_MESSAGES: {
        dispatch(actionMessages.addMessages(data));
        break;
      }
      case actionTypes.CONFIRM_MESSAGE: {
        dispatch(actionMessages.confirmMessage(data));
        break;
      }
      case actionTypes.RESPONSE_CHECK_SIGNATURE: {
        dispatch(actionMessages.responseCheckSignature(data));
        break;
      }
      case actionTypes.ADD_CHAT: {
        dispatch(actionChats.addChat({chatId: data.chatId, chatObject: data.chatInformation}));
        break;
      }
      case actionTypes.CHANGE_CHAT: {
        dispatch(actionChats.changeChat({chatId: data.chatId, chatObject: data.chatInformation}));
        break;
      }
      case actionTypes.ADD_CHATS: {
        dispatch(actionChats.addChats(data.chats));
        break;
      }
      case actionTypes.ADD_PEERS: {
        dispatch(actionPeers.addPeers(data.peers));
        break;
      }
      case actionTypes.SET_USERNAME: {
        dispatch(actionState.setUsername(data.username));
        socket.username = data.username;
        window.document.title = `CHATTeR - ${data.username}`;
        break;
      }
      default: {
        break;
      }
    }
  };

  const onClose = event => {
    if (Boolean(socket.username)) {
      window.location.reload();
    }
  };

  let socket = createSocket();
  socket.onmessage = onMessage;
  socket.onclose = onClose;

  return socket;
}
