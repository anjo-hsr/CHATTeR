import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* setConnection(socket) {
  yield take(actionTypes.SET_CONNECTION, action => {
    socket.send(JSON.stringify(action));
  });
}
