import {takeEvery} from 'redux-saga/effects';
import {sender} from './sender';

import {actionTypes} from '../actions/actions';

export function* addChat(socket) {
  yield takeEvery(actionTypes.ADD_CHAT, action => sender(socket, action));
}

export function* changeChat(socket) {
  yield takeEvery(actionTypes.CHANGE_CHAT, action => sender(socket, action));
}

export function* approveChat(socket) {
  yield takeEvery(actionTypes.APPROVE_CHAT, action => sender(socket, action));
}
