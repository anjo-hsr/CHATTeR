import {all, fork} from 'redux-saga/effects';

import {addChat, changeChat} from './chatSaga';
import {addMessage, getMessages} from './messageSaga';

export function* rootSaga(socket) {
  yield all([fork(addChat, socket), fork(changeChat, socket), fork(addMessage, socket), fork(getMessages, socket)]);
}
