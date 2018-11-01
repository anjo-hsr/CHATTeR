import axios from 'axios';

import {apiOptions} from '../../defaults/defaults';

export default {
  getPeers() {
    return axios.get(`${apiOptions.baseString}/API/peers`);
  },

  getChatHistory(address) {
    return axios.get(`${apiOptions.baseString}/API/chatHistory?address=${address}`);
  }
};
