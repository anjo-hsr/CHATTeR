import axios from 'axios';

import {apiOptions} from '../../defaults/defaults';

export default {
  deleteMessage(id) {
    axios
      .patch(
        `${apiOptions.baseString}/API/deleteMessage`,
        {id},
        {
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
          }
        }
      )
      .catch(err => console.error(err));
  },

  deletePeer(id) {
    axios
      .patch(
        `${apiOptions.baseString}/API/deletePeer`,
        {id},
        {
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
          }
        }
      )
      .catch(err => console.error(err));
  }
};
