import React from 'react';
import PropTypes from 'prop-types';

import {Form, Grid} from 'semantic-ui-react';

export default class MessageInput extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      message: ''
    };
  }

  handleChange = (event, target) => {
    this.setState({message: target.value});
  };

  render() {
    return (
      <Form
        onSubmit={() => {
          this.props.addMessage(this.props.chatId, {
            date: new Date(),
            author: this.props.username,
            message: this.state.message
          });
          this.setState({message: ''});
        }}
      >
        <Grid>
          <Grid.Row columns="equal">
            <Grid.Column>
              <Form.Input
                fluid
                label="New message"
                autoFocus
                placeholder="Enter Message"
                type="text"
                value={this.state.message}
                onChange={this.handleChange}
                required
              />
            </Grid.Column>
            <Grid.Column width="2" verticalAlign="bottom">
              <Form.Button content="Send" />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Form>
    );
  }
}

MessageInput.propTypes = {
  chatId: PropTypes.string.isRequired,
  addMessage: PropTypes.func.isRequired
};
