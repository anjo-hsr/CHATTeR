import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* addMessage(socket) {
  socket.send(JSON.stringify(action));
}
