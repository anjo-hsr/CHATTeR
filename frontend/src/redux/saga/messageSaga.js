import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* addMessage(socket) {
  yield takeEvery(actionTypes.ADD_MESSAGE, action => {
    socket.send(JSON.stringify(action));
  });
}

export function* getMessages(socket) {
  yield takeEvery(actionTypes.SELECT_CHAT, action => {
    socket.send(JSON.stringify(action));
  });
}
