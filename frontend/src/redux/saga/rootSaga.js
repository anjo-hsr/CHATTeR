import {all, fork} from 'redux-saga/effects';

import {addChat, approveChat, changeChat, leaveChat} from './chatSaga';
import {addMessage, getMessages} from './messageSaga';

export function* rootSaga(socket) {
  yield all([
    fork(addChat, socket),
    fork(approveChat, socket),
    fork(changeChat, socket),
    fork(leaveChat, socket),
    fork(addMessage, socket),
    fork(getMessages, socket)
  ]);
}
