import {connect} from 'react-redux';
import AppComponent from '../components/App';
import {actionState} from '../redux/actions/actions';

const mapStateToProps = dispatch => ({
  setSocketStateOpen: () => {
    dispatch(actionState.setSocketStateOpen());
  }
});

export const App = connect(
  reduxStore => ({
    isSocketOpen: reduxStore.state.isSocketOpen,
    selectedChat: reduxStore.state.selectedChat,
    username: reduxStore.state.username
  }),
  mapStateToProps
)(AppComponent);
