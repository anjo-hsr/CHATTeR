import {connect} from 'react-redux';
import MessageInputComponent from '../components/message/MessageInput';
import {actionMessages} from '../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  dispatch: message => {
    dispatch(() => actionMessages.addMessage);
  }
});

export const MessageInput = connect(
  () => ({}),
  mapDispatchToProps
)(MessageInputComponent);
