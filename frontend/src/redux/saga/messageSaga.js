import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';
import {sender} from './sender';

export function* addMessage(socket) {
  yield takeEvery(actionTypes.ADD_MESSAGE, action => sender(socket, action));
}

export function* checkSignature(socket) {
  yield takeEvery(actionTypes.CHECK_SIGNATURE, action => sender(socket, action));
}
