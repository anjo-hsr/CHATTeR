import {connect} from 'react-redux';
import AppComponent from '../components/App';
import {actionState} from '../redux/actions/actions';

const mapDispatchToProps = dispatch => ({
  setConnection: (username, ipAddress, portNumber) => {
    dispatch(actionState.setConnection(username, ipAddress, portNumber));
  }
});

export const App = connect(
  reduxStore => ({
    selectedChat: reduxStore.state.selectedChat,
    username: reduxStore.state.username
  }),
  mapDispatchToProps
)(AppComponent);
