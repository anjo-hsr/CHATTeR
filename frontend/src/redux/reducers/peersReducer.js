import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = [], action) {
  switch (action.type) {
    case actionTypes.ADD_PEERS: {
      return reduxStore.concat(action.peers);
    }

    default:
      return reduxStore;
  }
}
