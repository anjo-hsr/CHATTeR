import React from 'react';
import {Form, Grid, Modal} from 'semantic-ui-react';

export default class ModalSetUsername extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: ''
    };
  }

  handleChange = (event, target) => {
    this.setState({[target.name]: target.value});
  };

  render() {
    return (
      <Modal open={!Boolean(this.props.username)}>
        <Modal.Header content="Add new contact" />
        <Modal.Content>
          <Form onSubmit={() => this.props.setUsername(this.state.name)}>
            <Grid>
              <Grid.Row columns="equal">
                <Grid.Column>
                  <Form.Input
                    fluid
                    label="Username"
                    placeholder="Enter Username"
                    type="text"
                    name="name"
                    value={this.state.name}
                    onChange={this.handleChange}
                    required
                  />
                </Grid.Column>
              </Grid.Row>
              <Grid.Row columns="equal">
                <Grid.Column />
                <Grid.Column width="2">
                  <Form.Button color="green" basic content="Save" />
                </Grid.Column>
              </Grid.Row>
            </Grid>
          </Form>
        </Modal.Content>
      </Modal>
    );
  }
}
