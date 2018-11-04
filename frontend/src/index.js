import React from 'react';
import ReactDOM from 'react-dom';

import {Provider} from 'react-redux';
import {createStore, applyMiddleware} from 'redux';
import createSagaMiddleware from 'redux-saga';
import {composeWithDevTools} from 'redux-devtools-extension';

import {creator, helper} from './helpers/webSocketHelper';

import App from './components/App';
import registerServiceWorker from './registerServiceWorker';
import reducers from './redux/reducers/reducers';
import {actionChats, actionMessages, actionState} from './redux/actions/actions';

import './style/style.css';
import 'semantic-ui-css/semantic.min.css';

const sagaMiddleware = createSagaMiddleware();

const store = createStore(reducers, composeWithDevTools(applyMiddleware(sagaMiddleware)));
store.dispatch(actionChats.addChat({id: 0, peers: ['Pascal, Jonas, Me']}));
store.dispatch(actionState.selectChat(0));
store.dispatch(
  actionMessages.addMessage({
    chatId: 0,
    message: {
      date: '2018-10-01',
      author: 'Pascal',
      message: 'Hoi'
    }
  })
);

const socket = creator(store.dispatch, 'Andi');
sagaMiddleware.run(helper, {socket, username: 'Andi'});

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root')
);
registerServiceWorker();
