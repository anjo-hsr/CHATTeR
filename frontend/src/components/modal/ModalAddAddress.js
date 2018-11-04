import React from 'react';
import {Button, Form, Grid, Modal} from 'semantic-ui-react';

export default class ModalAddAddress extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: '',
      address: '',
      open: false
    };
  }

  handleChange = (event, target) => {
    this.setState({[target.name]: target.value});
  };

  render() {
    return (
      <Modal
        open={this.state.open}
        trigger={
          <Button
            className="addAddress"
            inverted
            icon="plus square outline"
            size="large"
            color="green"
            onClick={() => this.setState({open: true})}
          />
        }
      >
        <Modal.Header content="Add new contact" />
        <Modal.Content>
          <Form onSubmit={() => ({})}>
            <Grid>
              <Grid.Row columns="equal">
                <Grid.Column>
                  <Form.Input
                    fluid
                    label="Name of Chat"
                    placeholder="Enter Name"
                    type="text"
                    name="name"
                    value={this.state.name}
                    onChange={this.handleChange}
                    required
                  />
                </Grid.Column>
              </Grid.Row>
              <Grid.Row columns="equal">
                <Grid.Column>
                  <Form.Input
                    fluid
                    label="Address of Chat"
                    placeholder="Enter Address"
                    type="text"
                    name="address"
                    value={this.state.address}
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
