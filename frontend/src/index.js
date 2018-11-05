import React from 'react';
import ReactDOM from 'react-dom';

import {Provider} from 'react-redux';
import {createStore, applyMiddleware} from 'redux';
import createSagaMiddleware from 'redux-saga';
import {composeWithDevTools} from 'redux-devtools-extension';

import {App} from './containers/App';
import registerServiceWorker from './registerServiceWorker';
import reducers from './redux/reducers/reducers';

import './style/style.css';
import 'semantic-ui-css/semantic.min.css';
import actionPeers from './redux/actions/actionPeers';

const sagaMiddleware = createSagaMiddleware();
const store = createStore(reducers, composeWithDevTools(applyMiddleware(sagaMiddleware)));
store.dispatch(actionPeers.addPeers(['Pascal', 'Jonas', 'Tobias']));

ReactDOM.render(
  <Provider store={store}>
    <App middleware={sagaMiddleware} />
  </Provider>,
  document.getElementById('root')
);
registerServiceWorker();
