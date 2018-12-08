import React from 'react';
import PropTypes from 'prop-types';

import {Grid} from 'semantic-ui-react';

import {MessageHistory} from '../../containers/message/MessageHistory';
import {MessageInput} from '../../containers/message/MessageInput';
import {MessageWindowHeader} from '../../containers/message/MessageWindowHeader';
import {numbers} from '../../defaults/defaults';
import {Scrollbars} from 'react-custom-scrollbars';

export default class MessageWindow extends React.Component {
  render() {
    return (
      <Grid className={this.props.isSidebarOpen ? 'background' : ''}>
        <MessageWindowHeader />
        <Grid.Row className="messageHistory" id="messageHistory" columns="equal">
          <Grid.Column>
            <Scrollbars autoHide autoHideTimeout={numbers.autoHideTimeout} autoHideDuration={numbers.autoHideDuration}>
              <MessageHistory />
            </Scrollbars>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row className="messageInput" columns="equal">
          <Grid.Column>
            <MessageInput />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

MessageWindow.propTypes = {
  isSidebarOpen: PropTypes.bool.isRequired
};
