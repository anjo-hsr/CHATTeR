import React from 'react';
import PropTypes from 'prop-types';
import {Button, Form, Grid, Modal} from 'semantic-ui-react';

import Select from 'react-select';

export default class ModalAddAddress extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: '',
      selectedPeers: null,
      open: false
    };
  }

  handleTextChange = (event, target) => {
    this.setState({[target.name]: target.value});
  };

  handleSelectChange = selectedPeers => {
    this.setState({selectedPeers});
  };

  getRandomId = () => {
    const MIN_VALUE = 100;
    const MAX_VALUE = 100000;
    return Math.floor(Math.random() * (MAX_VALUE - MIN_VALUE + 1)) + MIN_VALUE;
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
          <Form
            onSubmit={() => {
              this.props.addChat({
                id: this.getRandomId(),
                name: this.state.name,
                peers: this.state.selectedPeers.map(peers => peers.label).concat(this.props.self)
              });
              this.setState({open: false});
            }}
          >
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
                    onChange={this.handleTextChange}
                    required
                  />
                  <Select
                    value={this.state.selectedPeers}
                    onChange={this.handleSelectChange}
                    options={this.props.peers}
                    placeholder="Select Peers"
                    isMulti
                  />
                </Grid.Column>
              </Grid.Row>
              <Grid.Row columns="equal">
                <Grid.Column />
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

ModalAddAddress.PropTypes = {
  addChat: PropTypes.func.isRequired,
  peers: PropTypes.arrayOf(PropTypes.string).isRequired
};
