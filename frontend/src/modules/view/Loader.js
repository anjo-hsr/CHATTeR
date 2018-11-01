import React from 'react';
import {Dimmer, Loader as Loading} from 'semantic-ui-react';

export default class Loader extends React.Component {
  render() {
    return (
      <Dimmer active inverted>
        <Loading inverted content="Loading Chats" />
      </Dimmer>
    );
  }
}
