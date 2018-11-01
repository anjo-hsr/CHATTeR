import React from 'react';
import {Form, Grid} from 'semantic-ui-react';

import {apiPost} from '../../helpers/apiHelpers';

export default class ChatInput extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      message: ''
    };
  }

  handleChange = (event, target) => {
    this.setState({message: target.value});
  };

  handleFormSubmit = () => {
    apiPost.postNewMessage(this.props.chatPartner.address, this.state.message);
    setTimeout(() => {
      this.setState({message: ''});
      this.props.updateChat(this.props.chatPartner.address);
      this.props.updatePeers();
    }, 500);
  };

  render() {
    return (
      <Form onSubmit={this.handleFormSubmit}>
        <Grid>
          <Grid.Row columns="equal">
            <Grid.Column>
              <Form.Input
                fluid
                label="New message"
                autoFocus={this.props.chatPartner.isOnline}
                disabled={!this.props.chatPartner.isOnline}
                placeholder="Enter Message"
                type="text"
                value={this.state.message}
                onChange={this.handleChange}
                required
              />
            </Grid.Column>
            <Grid.Column width="2" verticalAlign="bottom">
              <Form.Button className={this.props.chatPartner.isOnline ? '' : 'disabled'} content="Send" />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Form>
    );
  }
}
