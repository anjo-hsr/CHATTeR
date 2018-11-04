import {applyMiddleware, createStore} from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import promise from 'redux-promise-middleware';

import Sockette from 'sockette';

import reducer from './reducers/reducers';

const middleware = applyMiddleware(promise(), thunk, logger());

const webSocket = new Sockette(`ws://localhost:8000/chat`, {
  timeout: 5e3,
  maxAttempts: 10,
  onopen: event => handleOpening(event),
  onmessage: event => handleReceiving(event),
  onreconnect: event => handleReconnecting(event),
  onmaximum: event => handleMaximum(event),
  onclose: event => handleClosing(event),
  onerror: event => handleError(event)
});

export default {
  createStore() {
    createStore(reducer, middleware);
  },
  getWebSocket() {
    return webSocket;
  }
};
