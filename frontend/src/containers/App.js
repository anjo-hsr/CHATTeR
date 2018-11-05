import {connect} from 'react-redux';
import AppComponent from '../components/App';

export const App = connect(
  reduxStore => ({
    dispatch: reduxStore.dispatch,
    selectedChat: reduxStore.state.selectedChat,
    username: reduxStore.state.username
  }),
  {}
)(AppComponent);
