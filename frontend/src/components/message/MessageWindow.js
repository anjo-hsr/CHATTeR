import React from 'react';
import PropTypes from 'prop-types';

import {Scrollbars} from 'react-custom-scrollbars';
import {Grid} from 'semantic-ui-react';

import {MessageHistory} from '../../containers/message/MessageHistory';
import {MessageInput} from '../../containers/message/MessageInput';
import {MessageWindowHeader} from '../../containers/message/MessageWindowHeader';
import {numbers} from '../../defaults/defaults';

export default class MessageWindow extends React.Component {
  render() {
    return (
      <Grid className={this.props.isSidebarOpen ? 'background' : ''}>
        <MessageWindowHeader />
        <Grid.Row className="messageHistory" columns="equal">
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
