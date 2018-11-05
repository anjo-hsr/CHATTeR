import React from 'react';
import {Form, Grid, Modal} from 'semantic-ui-react';

export default class ModalSetUsername extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: window.localStorage.getItem('username') || '',
      ipAddress: window.localStorage.getItem('ipString') || '',
      portNumber: window.localStorage.getItem('portNumber') || '',
      isIpCorrect: true,
      isPortCorrect: true
    };
  }

  handleChange = (event, target) => {
    const CHECK_TIMEOUT_TIMER = 200;
    this.setState({[target.name]: target.value});
    setTimeout(() => {
      switch (target.name) {
        case 'ipAddress': {
          this.validateIpAddress();
          break;
        }
        case 'portNumber': {
          this.validatePort();
          break;
        }
      }
    }, CHECK_TIMEOUT_TIMER);
  };

  validateIpAddress = () => {
    const ipRegex = /^(([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\.){3}([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$/;
    const ipAddressCorrect = ipRegex.test(this.state.ipAddress);
    this.setState({
      isIpCorrect: ipAddressCorrect
    });
    return ipAddressCorrect;
  };

  validatePort = () => {
    const portNumber = this.state.portNumber;
    const MIN_PORT_NUMBER = 1;
    const MAX_PORT_NUMBER = Math.pow(2, 32) - 1;
    const portNumberCorrect = portNumber > MIN_PORT_NUMBER && portNumber < MAX_PORT_NUMBER;
    this.setState({
      isPortCorrect: portNumberCorrect
    });
    return portNumberCorrect;
  };

  checkInput = () => {
    const ipAddressCorrect = this.validateIpAddress();
    const portNumberCorrect = this.validatePort();
    return ipAddressCorrect && portNumberCorrect;
  };

  setLocalStorage = () => {
    window.localStorage.setItem('username', this.state.name);
    window.localStorage.setItem('ipString', this.state.ipAddress);
    window.localStorage.setItem('portNumber', this.state.portNumber);
  };

  render() {
    return (
      <Modal open={!Boolean(this.props.username)}>
        <Modal.Header content="Add new contact" />
        <Modal.Content>
          <Form
            onSubmit={() => {
              if (this.checkInput()) {
                this.setLocalStorage();
                this.props.setConnection(this.state.name, this.state.ipAddress, this.state.portNumber);
                this.props.setUsername(this.state.name);
              }
            }}
          >
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
                    autoFocus
                    required
                  />
                  <Form.Input
                    fluid
                    label="DHT IP address"
                    placeholder="Enter DHTs IP address"
                    type="text"
                    name="ipAddress"
                    value={this.state.ipAddress}
                    onChange={this.handleChange}
                    error={!this.state.isIpCorrect}
                    required
                  />
                  <Form.Input
                    fluid
                    label="DHT port number"
                    placeholder="Enter DHTs Port number"
                    type="text"
                    name="portNumber"
                    value={this.state.portNumber}
                    onChange={this.handleChange}
                    error={!this.state.isPortCorrect}
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
