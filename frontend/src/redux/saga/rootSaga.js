import {all, fork} from 'redux-saga/effects';

import {addChat, changeChat, leaveChat, getMessages} from './chatSaga';
import {addMessage, checkSignature} from './messageSaga';

export function* rootSaga(socket) {
  yield all([
    fork(addChat, socket),
    fork(changeChat, socket),
    fork(leaveChat, socket),
    fork(getMessages, socket),
    fork(addMessage, socket),
    fork(checkSignature, socket)
  ]);
}
