import {connect} from 'react-redux';
import ModalAddChatComponent from '../../components/modal/ModalAddChat';
import {actionChats, actionState} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  addChat: ({chatId, peers, name}) => {
    dispatch(actionChats.addChat({chatId, chatObject: {name, peers}}));
    dispatch(actionState.selectChat(chatId));
  }
});

export const ModalAddChat = connect(
  reduxStore => {
    return {
      peers: reduxStore.peers.map(peer => {
        return {value: peer.toLowerCase(), label: peer};
      }),
      self: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalAddChatComponent);
