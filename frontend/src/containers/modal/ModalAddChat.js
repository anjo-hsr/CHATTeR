import {connect} from 'react-redux';
import {actionChats, actionState} from '../../redux/actions/actions';
import shajs from 'sha.js';

import ModalAddChatComponent from '../../components/modal/ModalAddChat';

const mapDispatchToProps = dispatch => ({
  addChat: ({peers, name}) => {
    const chatId = shajs('sha256')
      .update(new Date() + Math.random())
      .digest('hex');
    dispatch(actionChats.addChat({chatId, chatObject: {name, peers}}));
    dispatch(actionState.selectChat(chatId));
    dispatch(actionChats.approveChat(chatId));
  }
});

const getSelectObjects = (peers, excludedPeer) => {
  return peers.filter(peer => peer.name !== excludedPeer).map(peer => {
    return {...peer, label: peer.name, value: peer.name};
  });
};

export const ModalAddChat = connect(
  reduxStore => {
    return {
      peers: getSelectObjects(reduxStore.peers, reduxStore.state.username),
      self: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalAddChatComponent);
