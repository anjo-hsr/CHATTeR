import {connect} from 'react-redux';
import AppComponent from '../components/App';

export const App = connect(
  reduxStore => ({
    selectedChat: reduxStore.state.selectedChat,
    username: reduxStore.state.username
  }),
  {}
)(AppComponent);
