import {connect} from 'react-redux';
import AppComponent from '../components/App';
import {actionState} from '../redux/actions/actions';

const mapStateToProps = dispatch => ({
  setSocketStateOpen: () => {
    dispatch(actionState.setSocketStateOpen());
  },
  toggleSidebar() {
    dispatch(actionState.toggleSidebar());
  },
  hideSidebar() {
    dispatch(actionState.hideSidebar());
  }
});

export const App = connect(
  reduxStore => ({
    isSocketOpen: reduxStore.state.isSocketOpen,
    selectedChat: reduxStore.state.selectedChat,
    username: reduxStore.state.username,
    sidebarIcon: reduxStore.state.sidebarIcon,
    isSidebarOpen: reduxStore.state.isSidebarOpen
  }),
  mapStateToProps
)(AppComponent);
