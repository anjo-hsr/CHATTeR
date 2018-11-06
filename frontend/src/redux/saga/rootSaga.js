import {take} from 'redux-saga/effects';

import {actionTypes} from '../actions/actions';

export function* rootSaga(socket) {
  yield take(action => {
    console.log(action.type);
    switch (action.type) {
      case actionTypes.ADD_MESSAGE:
      case actionTypes.SET_CONNECTION: {
        console.log(action.type);
        socket.send(JSON.stringify(action.message));
      }
    }
  });
}
