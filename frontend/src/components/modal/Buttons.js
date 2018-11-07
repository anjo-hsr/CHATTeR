import React from 'react';

import {isMobile} from 'react-device-detect';
import {Form, Grid} from 'semantic-ui-react';

export default class Buttons extends React.Component {
  render() {
    const width = isMobile ? 5 : 2;
    return (
      <Grid.Row columns="equal">
        <Grid.Column />
        {this.props.cancelNeeded && (
          <Grid.Column width={width}>
            <Form.Button color="orange" basic content="Cancel" onClick={event => this.props.closeAction(event)} />
          </Grid.Column>
        )}
        <Grid.Column width={width}>
          <Form.Button color="green" basic content="Save" />
        </Grid.Column>
      </Grid.Row>
    );
  }
}
