import React from 'react';

import {Container, Dimmer, Loader, Modal} from 'semantic-ui-react';

export default class ModalWaitForWebSocket extends React.Component {
  render() {
    return (
      <Modal open>
        <Modal.Content>
          <Container className="loadingModal">
            <Dimmer active inverted>
              <Loader size="massive" content="Please check the connection or restart CHATTeR" />
            </Dimmer>
          </Container>
        </Modal.Content>
      </Modal>
    );
  }
}
