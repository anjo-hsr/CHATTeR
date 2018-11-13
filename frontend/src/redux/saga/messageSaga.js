import {takeEvery} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';
import {sender} from './sender';

export function* addMessage(socket) {
  yield takeEvery(actionTypes.ADD_MESSAGE, action => sender(socket, action));
}

export function* getMessages(socket) {
  yield takeEvery(actionTypes.SELECT_CHAT, action => sender(socket, action));
}
