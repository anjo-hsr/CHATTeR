import {connect} from 'react-redux';
import ModalAddChatComponent from '../../components/modal/ModalAddChat';
import {actionChats, actionState} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  addChat: ({id, peers, name}) => {
    dispatch(actionChats.addChat({id, chatObject: {name, peers}}));
    dispatch(actionState.selectChat(id));
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
