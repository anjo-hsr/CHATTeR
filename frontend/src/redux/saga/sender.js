export function sender(socket, action) {
  socket.send(JSON.stringify(action));
}
