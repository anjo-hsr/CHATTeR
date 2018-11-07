import {connect} from 'react-redux';
import ModalSetConnectionComponent from '../../components/modal/ModalSetConnection';
import {actionState} from '../../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  setConnection: username => {
    dispatch(actionState.setUsername(username));
  }
});

export const ModalSetConnection = connect(
  reduxStore => {
    return {
      username: reduxStore.state.username
    };
  },
  mapDispatchToProps
)(ModalSetConnectionComponent);
