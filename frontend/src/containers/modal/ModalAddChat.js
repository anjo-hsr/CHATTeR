import {connect} from 'react-redux';
import ModalAddChatComponent from '../../components/modal/ModalAddChat';
import {actionChats, actionState} from '../../redux/actions/actions';
import shajs from 'sha.js';

const mapDispatchToProps = dispatch => ({
  addChat: ({peers, name}) => {
    const chatId = shajs('sha256')
      .update(new Date() + Math.random())
      .digest('hex');
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
