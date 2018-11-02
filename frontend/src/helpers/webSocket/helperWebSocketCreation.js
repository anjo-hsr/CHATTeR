import Sockette from 'sockette';

export default function subscribeToServer(name) {
  return new Sockette(`ws://localhost:8000/chat`, {
    timeout: 5e3,
    maxAttempts: 10,
    onopen: event => handleOpening(event),
    onmessage: event => handleReceiving(event),
    onreconnect: event => handleReconnecting(event),
    onmaximum: event => handleMaximum(event),
    onclose: event => handleClosing(event),
    onerror: event => handleError(event)
  });
}

function handleOpening(event) {
  console.log('Connected!', event);
}

function handleReceiving(event) {
  console.log('Received:', event);
}

function handleReconnecting(event) {
  console.log('Reconnecting...:', event);
}

function handleMaximum(event) {
  console.log('Stop Attempting!:', event);
}

function handleClosing(event) {
  console.log('Closed!:', event);
}

function handleError(event) {
  console.log('Error!:', event);
}
