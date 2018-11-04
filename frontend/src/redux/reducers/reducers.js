import {combineReducers} from 'redux';

import messages from './messagesReducer';
import chats from './chatsReducer';
import state from './stateReducer';

export default combineReducers({messages, chats, state});
