import axios from 'axios';

import {apiOptions} from '../../defaults/defaults';
import moment from 'moment';

export default {
  postNewPeers() {
    let newPeer = {name: this.state.name, address: this.state.address};
    axios
      .post(`${apiOptions.baseString}/API/addPeer`, newPeer, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(res => {
        this.setState({open: false});
      })
      .catch(err => console.error(err));
  },
  postNewMessage(address, message, updateChat) {
    axios
      .post(
        `${apiOptions.baseString}/API/addMessage`,
        {
          address,
          isMe: true,
          date: moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
          message
        },
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
