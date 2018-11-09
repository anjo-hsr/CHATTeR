import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* doPingPong(socket) {
  yield takeEvery(actionTypes.PING_PONG, action => {
    socket.send(JSON.stringify(action));
  });
}
