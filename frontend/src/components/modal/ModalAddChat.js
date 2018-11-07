import React from 'react';
import PropTypes from 'prop-types';
import {Button, Form, Grid, Modal} from 'semantic-ui-react';

import Select from 'react-select';
import Buttons from './Buttons';

export default class ModalAddAddress extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: '',
      selectedPeers: [],
      isAPeerSelected: true,
      open: false
    };
  }

  handleTextChange = (event, target) => {
    this.setState({[target.name]: target.value});
  };

  handleSelectChange = selectedPeers => {
    this.setState({selectedPeers});
  };

  checkPeers = () => {
    const check = this.state.selectedPeers.length > 0;
    this.setState({isAPeerSelected: check});
    return check;
  };

  getRandomId = () => {
    const MIN_VALUE = 100;
    const MAX_VALUE = 100000;
    return Math.floor(Math.random() * (MAX_VALUE - MIN_VALUE + 1)) + MIN_VALUE;
  };

  close = event => {
    event.preventDefault();
    this.setState({open: false});
  };

  render() {
    return (
      <Modal
        open={this.state.open}
        trigger={
          <Button
            className="addChat"
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
              if (this.checkPeers()) {
                this.props.addChat({
                  chatId: this.getRandomId().toString(),
                  name: this.state.name,
                  peers: this.state.selectedPeers.map(peers => peers.label).concat(this.props.self)
                });
                this.setState({open: false, name: '', selectedPeers: []});
              }
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
              <Buttons cancelNeeded={true} closeAction={this.close.bind(this)} />
            </Grid>
          </Form>
        </Modal.Content>
      </Modal>
    );
  }
}

ModalAddAddress.propTypes = {
  addChat: PropTypes.func.isRequired,
  peers: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      value: PropTypes.string.isRequired
    })
  ).isRequired
};
