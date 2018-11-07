import React from 'react';
import PropTypes from 'prop-types';
import {Button, Form, Grid, Modal} from 'semantic-ui-react';

import Buttons from './Buttons';

import Select from 'react-select';

export default class ModalChangeChat extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: this.props.name,
      selectedPeers: this.props.selectedPeers,
      open: false
    };
  }

  handleTextChange = (event, target) => {
    this.setState({[target.name]: target.value});
  };

  handleSelectChange = selectedPeers => {
    this.setState({selectedPeers});
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
            className="addAddress"
            inverted
            icon="edit outline"
            size="large"
            color="orange"
            onClick={() => this.setState({open: true})}
          />
        }
      >
        <Modal.Header content="Add new contact" />
        <Modal.Content>
          <Form
            onSubmit={() => {
              this.props.changeChat({
                chatId: this.props.selectedChat,
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
              <Buttons cancelNeeded={true} closeAction={this.close.bind(this)} />
            </Grid>
          </Form>
        </Modal.Content>
      </Modal>
    );
  }
}

ModalChangeChat.propTypes = {
  changeChat: PropTypes.func.isRequired,
  peers: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      value: PropTypes.string.isRequired
    })
  ).isRequired,
  selectedChat: PropTypes.string.isRequired,
  selectedPeers: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      value: PropTypes.string.isRequired
    })
  ).isRequired
};
