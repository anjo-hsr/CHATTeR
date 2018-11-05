import {connect} from 'react-redux';
import ModalSetUsernameComponent from '../../components/modal/ModalSetUsername';
import {actionState} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  setConnection: (name, ipAddress, portNumber) => {
    dispatch(actionState.setConnection(name, ipAddress, portNumber));
  },
  setUsername: username => {
    dispatch(actionState.setUsername(username));
  }
});

export const ModalSetUsername = connect(
  reduxStore => {
    return {
      username: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalSetUsernameComponent);
