import {actionTypes as types} from './actions';

export default {
  addPeers(peers) {
    return {
      type: types.ADD_PEERS,
      peers
    };
  }
};
