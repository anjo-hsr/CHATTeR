export default function sendMessage(webSocket, message) {
  webSocket.send(JSON.stringify(message));
}
