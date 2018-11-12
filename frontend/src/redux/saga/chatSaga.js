import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* addChat(socket) {
  yield takeEvery(actionTypes.ADD_CHAT, action => {
    socket.send(JSON.stringify(action));
  });
}

export function* changeChat(socket) {
  yield takeEvery(actionTypes.CHANGE_CHAT, action => {
    socket.send(JSON.stringify(action));
  });
}
