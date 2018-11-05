import {combineReducers} from 'redux';

import messages from './messagesReducer';
import chats from './chatsReducer';
import state from './stateReducer';
import peers from './peersReducer';

export default combineReducers({messages, chats, state, peers});
