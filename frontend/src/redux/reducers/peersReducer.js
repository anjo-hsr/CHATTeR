import {actionTypes} from '../actions/actions';

export default function reducer(reduxStore = [], action) {
  switch (action.type) {
    case actionTypes.ADD_PEERS: {
      return mergeArrays(reduxStore, action.peers);
    }

    default:
      return reduxStore;
  }
}

const mergeArrays = (originalStore, peers) => {
  const uniquePeers = peers.filter(peer => originalStore.indexOf(peer) === -1);
  return originalStore.concat(uniquePeers);
};
