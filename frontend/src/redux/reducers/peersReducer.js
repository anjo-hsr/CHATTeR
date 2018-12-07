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
  const uniquePeers = peers.filter(peer => !originalStore.some(element => element.name === peer.name));
  const duplicatedPeers = peers.filter(peer => originalStore.some(element => element.name === peer.name));
  let mergedPeers = originalStore.concat(uniquePeers);

  return mergedPeers.map(peer => {
    if (duplicatedPeers.some(element => element.name === peer.name)) {
      const newPeerOption = duplicatedPeers.find(element => element.name === peer.name);
      if (typeof newPeerOption.isOnline === 'boolean' && Boolean(newPeerOption.etherAddress)) {
        peer = newPeerOption;
      }
    }
    return peer;
  });
};
