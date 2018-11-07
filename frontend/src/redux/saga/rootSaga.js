import {all, fork} from 'redux-saga/effects';

import {addChat, changeChat, deleteChat} from './chatSaga';
import {addMessage, getMessages} from './messageSaga';
import {setConnection} from './connectionSaga';

export function* rootSaga(socket) {
  yield all([
    fork(addChat, socket),
    fork(changeChat, socket),
    fork(deleteChat, socket),
    fork(addMessage, socket),
    fork(getMessages, socket),
    fork(setConnection, socket)
  ]);
}
