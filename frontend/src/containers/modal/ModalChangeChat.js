import {connect} from 'react-redux';
import {actionChats} from '../../redux/actions/actions';

import ModalChangeChatComponent from '../../components/modal/ModalChangeChat';

const mapDispatchToProps = dispatch => ({
  changeChat: ({chatId, peers, name}) => {
    dispatch(actionChats.changeChat({chatId, chatObject: {name, peers}}));
  }
});

const filterPeers = (peers, excludedPeer) => {
  return peers
    .filter(peer => peer.name !== excludedPeer)
    .map(peer => {
      return {...peer, label: peer.name, value: peer.name};
    });
};

const filterSelectedPeers = (peers, excludedPeer) => {
  return peers
    .filter(peer => peer !== excludedPeer)
    .map(peer => {
      return {label: peer, value: peer};
    });
};

export const ModalChangeChat = connect(
  reduxStore => {
    return {
      selectedChat: reduxStore.state.selectedChat,
      name: reduxStore.chats[reduxStore.state.selectedChat].name,
      selectedPeers: filterSelectedPeers(
        reduxStore.chats[reduxStore.state.selectedChat].peers,
        reduxStore.state.username
      ),
      peers: filterPeers(reduxStore.peers),
      self: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalChangeChatComponent);
