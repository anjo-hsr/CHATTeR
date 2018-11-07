import {connect} from 'react-redux';
import ModalChangeChatComponent from '../../components/modal/ModalChangeChat';
import {actionChats} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  changeChat: ({chatId, peers, name}) => {
    dispatch(actionChats.changeChat({chatId, chatObject: {name, peers}}));
  }
});

const getSelectObjects = (peers, excludedPeer) => {
  return peers.filter(peer => peer !== excludedPeer).map(peer => ({value: peer.toLowerCase(), label: peer}));
};

export const ModalChangeChat = connect(
  reduxStore => {
    return {
      selectedChat: reduxStore.state.selectedChat,
      name: reduxStore.chats[reduxStore.state.selectedChat].name,
      selectedPeers: getSelectObjects(reduxStore.chats[reduxStore.state.selectedChat].peers, reduxStore.state.username),
      peers: getSelectObjects(reduxStore.peers),
      self: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalChangeChatComponent);
